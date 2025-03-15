<template>
  <div class="pa-4 text-center">
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
      alert('O estudante associado à tese é obrigatório')
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
