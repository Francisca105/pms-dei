<template>
  <v-row align="center">
    <v-col>
      <h2 class="text-left ml-1">
        Escolha os Professores para o Júri da Tese {{ $route.params.id }}
      </h2>
    </v-col>
  </v-row>

  <v-text-field
    v-model="search"
    label="Pesquisar"
    prepend-inner-icon="mdi-magnify"
    variant="outlined"
    hide-details
    single-line
  ></v-text-field>

  <v-data-table
    :headers="headers"
    :items="teachers"
    :search="search"
    :loading="loading"
    :custom-filter="fuzzySearch"
    item-key="id"
    class="text-left"
    no-data-text="Sem professores a apresentar."
    show-select
    v-model="selectedTeachers"
  >
  </v-data-table>

  <div class="mt-4">
    <v-btn color="primary" @click="submitProposal">Submeter Proposta de Júri</v-btn>
  </div>
</template>

<script setup lang="ts">
import type PeopleDto from '@/models/PeopleDto'
import RemoteService from '@/services/RemoteService'
import { reactive, ref } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const thesisId = route.params.id
const search = ref('')
const loading = ref(true)
const selectedTeachers = ref<PeopleDto[]>([])

const headers = [
  { title: 'ID', key: 'id', sortable: true, filterable: false },
  {
    title: 'Nome',
    key: 'name',
    sortable: true,
    filterable: true
  },
  {
    title: 'IST ID',
    key: 'istId',
    sortable: true,
    filterable: true
  },
  {
    title: 'Email',
    key: 'email',
    sortable: true,
    filterable: true
  }
]

const teachers = reactive<PeopleDto[]>([])

getTeachers()
async function getTeachers() {
  try {
    const teacherData = await RemoteService.getTeachers()
    teachers.splice(0, teachers.length)
    teachers.push(...teacherData)
  } catch (error) {
    console.error('Error fetching teachers:', error)
  } finally {
    loading.value = false
  }
}

const fuzzySearch = (value: string, search: string) => {
  if (!value || !search) return true
  const searchRegex = new RegExp(search.split('').join('.*'), 'i')
  return searchRegex.test(value)
}

async function submitProposal() {
  const teachers = selectedTeachers.value

  console.log('Submitting proposal:', teachers)

  try {
    await RemoteService.submitThesisProposal(thesisId, teachers)
    selectedTeachers.value = []
  } catch (error) {
    console.error(error)
  }
}
</script>
