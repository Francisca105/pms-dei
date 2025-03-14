<template>
  <v-dialog v-model="deleteDialogVisible" max-width="400">
    <v-card prepend-icon="mdi-account" title="Apagar Pessoa">
      <v-card-text>
        <v-text-field label="Nome*" required v-model="updatedPerson.name"></v-text-field>
        <v-text-field label="IST ID*" required v-model="updatedPerson.istId"></v-text-field>
        <v-text-field label="Email*" required v-model="updatedPerson.email"></v-text-field>

        <v-select
          :items="['Coordenador', 'Staff', 'Aluno', 'Professor', 'SC']"
          label="Categoria*"
          required
          v-model="updatedPerson.type"
        ></v-select>
      </v-card-text>

      <v-divider></v-divider>

      <v-card-actions>
        <v-btn text @click="closeDialog">Cancel</v-btn>
        <v-spacer></v-spacer>
        <v-btn color="primary" text @click="deletePerson">Delete</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import type PersonDto from '@/models/people/PersonDto'
import RemoteService from '@/services/RemoteService'

// Props and emits
const props = defineProps({
  person: {
    type: Object as () => PersonDto, // Define the `person` prop as required and typed
    required: true
  }
})
const emit = defineEmits(['close-delete-dialog', 'person-deleted'])

const typeMappings = {
  Coordenador: 'COORDINATOR',
  Staff: 'STAFF',
  Aluno: 'STUDENT',
  Professor: 'TEACHER',
  SC: 'SC'
}

const deleteDialogVisible = ref(false) // Controls dialog visibility

const getKeyByValue = (value: string) => {
  return (
    Object.keys(typeMappings).find(
      (key) => typeMappings[key as keyof typeof typeMappings] === value
    ) || null
  )
}

let updatedPerson = ref<PersonDto>({ ...props.person }) // Holds the updated person data

watch(
  () => props.person,
  (newPerson) => {
    if (!newPerson) return
    updatedPerson.value = { ...newPerson }
    updatedPerson.value.type = getKeyByValue(updatedPerson.value.type)
    deleteDialogVisible.value = true
  },
  { immediate: true } // Watch the `person` prop and update the `updatedPerson` ref
)

const closeDialog = () => {
  deleteDialogVisible.value = false
  emit('close-delete-dialog')
}

const deletePerson = async () => {
  try {
    await RemoteService.deletePerson(updatedPerson.value)
    emit('person-deleted', updatedPerson.value)
    closeDialog()
  } catch (error) {
    console.error('Failed to delete person:', error)
  }
}
</script>
