<template>
  <v-dialog v-model="editDialogVisible" max-width="400">
    <v-card prepend-icon="mdi-account" title="Editar Pessoa">
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
        <v-spacer></v-spacer>
        <v-btn text @click="closeDialog">Cancel</v-btn>
        <v-btn color="primary" text @click="savePerson">Save</v-btn>
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
const emit = defineEmits(['close-edit-dialog', 'person-edited'])

const typeMappings = {
  Coordenador: 'COORDINATOR',
  Staff: 'STAFF',
  Aluno: 'STUDENT',
  Professor: 'TEACHER',
  SC: 'SC'
}

const editDialogVisible = ref(false) // Controls dialog visibility

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
    editDialogVisible.value = true
  },
  { immediate: true } // Watch the `person` prop and update the `updatedPerson` ref
)

const closeDialog = () => {
  editDialogVisible.value = false
  emit('close-edit-dialog')
}

const savePerson = async () => {
  updatedPerson.value.type = typeMappings[updatedPerson.value.type as keyof typeof typeMappings]
  try {
    await RemoteService.updatePerson(updatedPerson.value)
    emit('person-edited', updatedPerson.value)
    closeDialog()
  } catch (error) {
    console.error('Failed to save person:', error)
  }
}
</script>
