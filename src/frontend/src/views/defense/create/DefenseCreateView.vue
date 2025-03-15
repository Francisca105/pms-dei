<template>
  <div class="pa-4">
    <h1 class="text-h4 text-center mb-4">Sistema de Defesa de Teses</h1>

    <!-- Documentation Section -->
    <v-card class="mb-6" variant="outlined">
      <v-card-title class="d-flex align-center">
        <v-icon icon="mdi-information-outline" class="mr-2"></v-icon>
        Documentação
      </v-card-title>
      <v-card-text>
        <v-expansion-panels>
          <v-expansion-panel>
            <v-expansion-panel-title>Como criar uma nova defesa</v-expansion-panel-title>
            <v-expansion-panel-text>
              <ol>
                <li>Clique no botão <b>"Criar Defesa"</b></li>
                <li>Selecione o estudante que irá ficar associado à Defesa no menu suspenso</li>
                <li>Clique em "Criar" para confirmar</li>
              </ol>
              <p>
                Após a criação, a Defesa será associada ao estudante selecionado e aparecerá na
                lista de Defesas.
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
            <v-expansion-panel-title>Fluxo de trabalho de defesa</v-expansion-panel-title>
            <v-expansion-panel-text>
              <p>O sistema de defesa segue o seguinte fluxo de trabalho:</p>
              <ol>
                <li><strong>Defesa Agendada:</strong> O coordenador agenda a defesa</li>
                <li>
                  <strong>Em Revisão:</strong> Quando a data atual é posterior à data de agendamento
                </li>
                <li>
                  <strong>Submetido ao Fenix:</strong> O coordenador atribui uma nota ao estudante e
                  submete ao Fenix
                </li>
              </ol>
            </v-expansion-panel-text>
          </v-expansion-panel>

          <v-expansion-panel>
            <v-expansion-panel-title>Requisitos e restrições</v-expansion-panel-title>
            <v-expansion-panel-text>
              <ul>
                <li>Cada defesa está associada a uma Defesa</li>
                <li>Um estudante só pode ter uma Defesa e por consequência uma defesa</li>
                <li>Apenas usuários autorizados podem gerenciar defesas</li>
              </ul>
            </v-expansion-panel-text>
          </v-expansion-panel>
        </v-expansion-panels>
      </v-card-text>
    </v-card>

    <!-- Create Defense -->
    <div class="text-center">
      <v-dialog v-model="dialog" max-width="600">
        <template v-slot:activator="{ props: activatorProps }">
          <v-btn
            class="text-none font-weight-regular"
            prepend-icon="mdi-plus"
            text="Criar Defesa"
            v-bind="activatorProps"
            color="primary"
          ></v-btn>
        </template>

        <v-card prepend-icon="mdi-book" title="Nova Defesa">
          <v-card-text>
            <!-- Student -->
            <v-select
              :items="students"
              item-title="name"
              item-value="id"
              label="Estudante*"
              required
              v-model="newDefense.student"
              :rules="[(v) => !!v || 'Estudante é obrigatório']"
            ></v-select>
          </v-card-text>

          <v-divider></v-divider>

          <v-card-actions>
            <v-spacer></v-spacer>

            <v-btn text="Fechar" variant="plain" @click="dialog = false"></v-btn>

            <v-btn color="primary" text="Criar" variant="tonal" @click="saveDefense"></v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import RemoDefesarvice from '@/services/RemoteService'
import type DefenseWorkflowDto from '@/models/defense/DefenseWorkflowDto'
import type PersonDto from '@/models/person/PersonDto'

const dialog = ref(false)
const students = ref<PersonDto[]>([])
const emit = defineEmits(['defense-created'])

const newDefense = ref<DefenseWorkflowDto>({
  student: null
})

onMounted(async () => {
  students.value = await RemoDefesarvice.getStudents()
})

const saveDefense = async () => {
  try {
    if (newDefense.value.student == null) {
      alert('Estudante é obrigatório')
      return
    }

    await RemoDefesarvice.createDefense(newDefense.value.student)
    dialog.value = false
    newDefense.value = {
      student: null
    }
    emit('defense-created')
  } catch (error) {
    console.error('Erro ao criar Defesa:', error)
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
