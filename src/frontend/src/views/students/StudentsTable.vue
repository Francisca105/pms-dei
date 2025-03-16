<template>
  <v-row align="center">
    <v-col>
      <h2 class="text-left ml-1">Listagem de Estudantes</h2>
    </v-col>
  </v-row>

  <v-row>
    <v-col cols="12" sm="6">
      <v-select
        v-model="thesisFilter"
        :items="thesisStatusOptions"
        label="Filtrar por Estado da Tese"
        clearable
        variant="outlined"
      ></v-select>
    </v-col>
    <v-col cols="12" sm="6">
      <v-select
        v-model="defenseFilter"
        :items="defenseStatusOptions"
        label="Filtrar por Estado da Defesa"
        clearable
        variant="outlined"
      ></v-select>
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
    :items="filteredPeople"
    :search="search"
    :loading="loading"
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

    <template v-slot:[`item.actions`]="{ item }">
      <v-btn
        v-if="item.thesisId"
        :to="`/thesis/${item.thesisId}`"
        variant="outlined"
        color="primary"
        class="mr-2"
      >
        Ver Tese
      </v-btn>
      <v-btn
        v-if="item.defenseId"
        :to="`/defense/${item.defenseId}`"
        variant="outlined"
        color="primary"
      >
        Ver Defesa
      </v-btn>
    </template>
  </v-data-table>
</template>

<script setup lang="ts">
import type PeopleDto from '@/models/PeopleDto'
import RemoteService from '@/services/RemoteService'
import { reactive, ref, computed } from 'vue'

let search = ref('')
let loading = ref(true)
let thesisFilter = ref('')
let defenseFilter = ref('')

const thesisStatusOptions = [
  'PROPOSAL_SUBMITTED',
  'APPROVED_SC',
  'PRESIDENT_ASSIGNED',
  'DOCUMENT_SIGNED',
  'SUBMITTED_FENIX',
  'Sem Tese'
]

const defenseStatusOptions = [
  'NOT_SCHEDULED',
  'SCHEDULED_DEFENSE',
  'UNDER_REVIEW',
  'SUBMITTED_FENIX',
  'Sem Defesa'
]

const headers = [
  { title: 'ID', key: 'id', sortable: true },
  { title: 'Nome', key: 'name', sortable: true },
  { title: 'IST ID', key: 'istId', sortable: true },
  { title: 'Email', key: 'email', sortable: true },
  { title: 'Estado da Tese', key: 'thesisStatus', sortable: true },
  { title: 'Estado da Defesa', key: 'defenseStatus', sortable: true },
  { title: 'Ações', key: 'actions', sortable: false }
]

let people: PeopleDto[] = reactive([])

const filteredPeople = computed(() => {
  return people.filter((person) => {
    const matchesSearch =
      fuzzySearch(person.name, search.value) ||
      fuzzySearch(person.email, search.value) ||
      fuzzySearch(person.istId, search.value)

    const matchesThesis = !thesisFilter.value || person.thesisStatus === thesisFilter.value
    const matchesDefense = !defenseFilter.value || person.defenseStatus === defenseFilter.value

    return matchesSearch && matchesThesis && matchesDefense
  })
})

async function getPeople() {
  loading.value = true
  const students = await RemoteService.getStudents()
  const thesisStatuses = await RemoteService.getThesis()
  const defenseStatuses = await RemoteService.getDefense()

  console.log(thesisStatuses)
  console.log(defenseStatuses)
  const studentsComplete = students.map((student) => {
    console.log(student)
    const thesis = thesisStatuses.find((t) => t.student.id === student.id)
    const defense = defenseStatuses.find((d) => d.thesisWorkflowId === thesis?.id)
    return {
      ...student,
      thesisId: thesis?.id || null,
      defenseId: defense?.id || null,
      thesisStatus: thesis ? thesis.state : 'Sem Tese',
      defenseStatus: defense ? defense.state : 'Sem Defesa'
    }
  })

  people.splice(0, people.length, ...studentsComplete)
  loading.value = false
}

const fuzzySearch = (value: string, search: string) => {
  if (!value) return false
  const searchRegex = new RegExp(search.split('').join('.*'), 'i')
  return searchRegex.test(value)
}

getPeople()
</script>
