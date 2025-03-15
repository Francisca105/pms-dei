<template>
  <v-row align="center">
    <v-col>
      <h2 class="text-left ml-1">Listagem de Estudantes</h2>
    </v-col>
    <v-col cols="auto">
      <CreatePersonDialog @person-created="getPeople" />
    </v-col>
  </v-row>

  <v-text-field
    v-model="search"
    label="Search"
    prepend-inner-icon="mdi-magnify"
    variant="outlined"
    hide-details
    single-line
  ></v-text-field>

  <v-data-table
    :headers="headers"
    :items="people"
    :search="search"
    :loading="loading"
    :custom-filter="fuzzySearch"
    item-key="id"
    class="text-left"
    no-data-text="Sem pessoas a apresentar."
  >
    <template v-slot:[`item.thesisStatus`]="{ item }">
      <v-chip v-if="item.thesisStatus === 'PROPOSAL_SUBMITTED'" color="purple" text-color="white">
        Proposta Submetida
      </v-chip>
      <v-chip v-else-if="item.thesisStatus === 'APPROVED_SC'" color="blue" text-color="white">
        Aprovado pelo SC
      </v-chip>
      <v-chip
        v-else-if="item.thesisStatus === 'PRESIDENT_ASSIGNED'"
        color="orange"
        text-color="white"
      >
        Presidente Atribuído
      </v-chip>
      <v-chip v-else-if="item.thesisStatus === 'DOCUMENT_SIGNED'" color="green" text-color="white">
        Documento Assinado
      </v-chip>
      <v-chip v-else-if="item.thesisStatus === 'SUBMITTED_FENIX'" color="red" text-color="white">
        Submetido no Fenix
      </v-chip>
      <v-chip v-else color="grey" text-color="white"> Sem Tese </v-chip>
    </template>

    <template v-slot:[`item.defenseStatus`]="{ item }">
      <v-chip v-if="item.defenseStatus === 'NOT_SCHEDULED'" color="grey" text-color="white">
        Não Agendada
      </v-chip>
      <v-chip
        v-else-if="item.defenseStatus === 'SCHEDULED_DEFENSE'"
        color="blue"
        text-color="white"
      >
        Defesa Agendada
      </v-chip>
      <v-chip v-else-if="item.defenseStatus === 'UNDER_REVIEW'" color="orange" text-color="white">
        Em Revisão
      </v-chip>
      <v-chip v-else-if="item.defenseStatus === 'SUBMITTED_FENIX'" color="red" text-color="white">
        Submetido no Fenix
      </v-chip>
      <v-chip v-else color="grey" text-color="white"> Sem Defesa </v-chip>
    </template>
  </v-data-table>
</template>

<script setup lang="ts">
import type PeopleDto from '@/models/PeopleDto'
import RemoteService from '@/services/RemoteService'
import { reactive, ref } from 'vue'

let search = ref('')
let loading = ref(true)
let editDialog = ref(false)
let deleteDialog = ref(false)

const headers = [
  { title: 'ID', key: 'id', value: 'id', sortable: true, filterable: false },
  {
    title: 'Nome',
    key: 'name',
    value: 'name',
    sortable: true,
    filterable: true
  },
  {
    title: 'IST ID',
    key: 'istId',
    value: 'istId',
    sortable: true,
    filterable: true
  },
  {
    title: 'Email',
    key: 'email',
    value: 'email',
    sortable: true,
    filterable: true
  },
  {
    title: 'Estado da Tese',
    key: 'thesisStatus',
    value: 'thesisStatus',
    sortable: true,
    filterable: true
  },
  {
    title: 'Estado da Defesa',
    key: 'defenseStatus',
    value: 'defenseStatus',
    sortable: true,
    filterable: true
  }
  // TODO: Add actions
  // {
  //   title: 'Ações',
  //   key: 'actions',
  //   sortable: false,
  //   filterable: false
  // }
]

let people: PeopleDto[] = reactive([])

getPeople()
async function getPeople() {
  loading.value = true

  const students = await RemoteService.getStudents()
  const thesisStatuses = await RemoteService.getThesis()
  const defenseStatuses = await RemoteService.getDefense()

  const studentsComplete = students.map((student) => {
    const thesis = thesisStatuses.find((t) => t.student.id === student.id)
    const defense = defenseStatuses.find((d) => d.student.id === student.id)
    return {
      ...student,
      thesisStatus: thesis ? thesis.state : 'Sem Tese',
      defenseStatus: defense ? defense.state : 'Sem Defesa'
    }
  })

  people.splice(0, people.length, ...studentsComplete)
  loading.value = false
}

const fuzzySearch = (value: string, search: string) => {
  // Regex to match any character in between the search characters
  if (!value) return false
  let searchRegex = new RegExp(search.split('').join('.*'), 'i')
  return searchRegex.test(value)
}
</script>
