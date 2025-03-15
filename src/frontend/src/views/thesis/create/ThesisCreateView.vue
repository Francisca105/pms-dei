<template>
  <div class="pa-4">
    <h1 class="text-h4 text-center mb-4">Sistema de Teses</h1>

    <!-- Documentation Section -->
    <v-card class="mb-6" variant="outlined">
      <v-card-title class="d-flex align-center">
        <v-icon icon="mdi-information-outline" class="mr-2"></v-icon>
        Documentação
      </v-card-title>
      <v-card-text>
        <v-expansion-panels>
          <v-expansion-panel>
            <v-expansion-panel-title>Como criar uma nova tese</v-expansion-panel-title>
            <v-expansion-panel-text>
              <ol>
                <li>Clique no botão <b>"Criar Tese"</b></li>
                <li>Selecione o estudante que irá ficar associado à tese no menu suspenso</li>
                <li>Clique em "Criar" para confirmar</li>
              </ol>
              <p>
                Após a criação, a tese será associada ao estudante selecionado e aparecerá na lista
                de teses.
              </p>
              <br />
              <p>
                <b>Nota:</b><br />
                Esta implementação foi realizada desta forma devido à falta de um sistema de login.
                Se um sistema de login fosse implementado, a seleção do estudante não seria
                necessária, pois o sistema identificaria automaticamente o estudante autenticado.
              </p>
            </v-expansion-panel-text>
          </v-expansion-panel>

          <v-expansion-panel>
            <v-expansion-panel-title>Fluxo de trabalho das teses</v-expansion-panel-title>
            <v-expansion-panel-text>
              <p>O sistema de teses segue o seguinte fluxo de trabalho:</p>
              <ol>
                <li><strong>Criação:</strong> A tese é criada e associada a um estudante</li>
                <li>
                  <strong>Proposta Juri:</strong> O estudante submete uma proposta de júri para
                  avaliação do SC
                </li>
                <li>
                  <strong>Revisão:</strong> O SC avalia a proposta de júri (e aprova-a para avançar
                  no fluxo)
                </li>
                <li>
                  <strong>Presidente Juri:</strong> O Coordenador escolhe o presidente do júri
                </li>
                <li>
                  <strong>Download:</strong> O coordenador faz download do documento (documento pdf
                  que contém informação sobre a tese e o júri), assina-o e faz upload do documento
                  assinado
                </li>
                <li><strong>Fenix:</strong> O Staff submete o documento ao Fenix (simulado)</li>
              </ol>
            </v-expansion-panel-text>
          </v-expansion-panel>

          <v-expansion-panel>
            <v-expansion-panel-title>Requisitos e restrições</v-expansion-panel-title>
            <v-expansion-panel-text>
              <ul>
                <li>Cada tese está associada a um estudante</li>
                <li>Um estudante só pode ter uma tese</li>
                <li>Apenas usuários autorizados podem gerenciar teses</li>
              </ul>
            </v-expansion-panel-text>
          </v-expansion-panel>
        </v-expansion-panels>
      </v-card-text>
    </v-card>

    <!-- Create Thesis Section -->
    <div class="text-center">
      <v-dialog v-model="dialog" max-width="600">
        <template v-slot:activator="{ props: activatorProps }">
          <v-btn
            class="text-none font-weight-regular"
            prepend-icon="mdi-plus"
            text="Criar Tese"
            v-bind="activatorProps"
            color="primary"
          ></v-btn>
        </template>

        <v-card prepend-icon="mdi-book" title="Nova Tese">
          <v-card-text>
            <!-- Student -->
            <v-select
              :items="students"
              item-title="name"
              item-value="id"
              label="Estudante*"
              required
              v-model="newThesis.student"
              :rules="[(v) => !!v || 'Estudante é obrigatório']"
            ></v-select>
          </v-card-text>

          <v-divider></v-divider>

          <v-card-actions>
            <v-spacer></v-spacer>

            <v-btn text="Fechar" variant="plain" @click="dialog = false"></v-btn>

            <v-btn color="primary" text="Criar" variant="tonal" @click="saveThesis"></v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import RemoteService from '@/services/RemoteService'
import type ThesisWorkflowDto from '@/models/thesis/ThesisWorkflowDto'
import type PersonDto from '@/models/person/PersonDto'

const dialog = ref(false)
const students = ref<PersonDto[]>([])
const emit = defineEmits(['thesis-created'])

const newThesis = ref<ThesisWorkflowDto>({
  student: null
})

onMounted(async () => {
  students.value = await RemoteService.getStudents()
})

const saveThesis = async () => {
  try {
    if (newThesis.value.student == null) {
      alert('Estudante é obrigatório')
      return
    }
    await RemoteService.createThesis(newThesis.value.student)
    dialog.value = false
    newThesis.value = {
      student: null
    }
    emit('thesis-created')
  } catch (error) {
    console.error('Erro ao criar tese:', error)
  }
}
</script>

<style scoped>
.v-expansion-panels {
  margin-top: 16px;
  margin-bottom: 16px;
}
.v-expansion-panel-text {
  text-align: left;
}
</style>
