<template>
  <v-row align="center">
    <v-col>
      <h2 class="text-left ml-1">Documentos Assinados Pendentes de Submissão ao Fenix</h2>
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
    :items="pendingTheses"
    :search="search"
    :loading="loading"
    item-key="id"
    class="text-left"
    no-data-text="Sem documentos assinados pendentes."
  >
    <template v-slot:item.student="{ item }">
      {{ item.student.name }} ({{ item.student.istId }})
    </template>
    <template v-slot:item.jury="{ item }">
      <ul>
        <li v-for="teacher in item.jury" :key="teacher.id">
          {{ teacher.name }} ({{ teacher.istId }})
        </li>
      </ul>
    </template>
    <template v-slot:item.president="{ item }">
      {{ item.president ? item.president.name : 'N/A' }}
    </template>
    <template v-slot:item.actions="{ item }">
      <v-btn color="primary" @click="submitToFenix(item)">Submeter ao Fenix</v-btn>
    </template>
  </v-data-table>
</template>

<script setup lang="ts">
import type ThesisDto from '@/models/ThesisDto'
import RemoteService from '@/services/RemoteService'
import { reactive, ref } from 'vue'

const search = ref('')
const loading = ref(true)

const headers = [
  { title: 'ID', key: 'id', sortable: true, filterable: false },
  {
    title: 'Estudante',
    key: 'student',
    sortable: true,
    filterable: true
  },
  {
    title: 'Júri',
    key: 'jury',
    sortable: true,
    filterable: true
  },
  {
    title: 'Presidente do Júri',
    key: 'president',
    sortable: true,
    filterable: true
  },
  {
    title: 'Ações',
    key: 'actions',
    sortable: false,
    filterable: false
  }
]

const pendingTheses = reactive<ThesisDto[]>([])

getPendingTheses()
async function getPendingTheses() {
  try {
    const thesisData = await RemoteService.getThesisDocumentSigned()
    pendingTheses.splice(0, pendingTheses.length)
    pendingTheses.push(...thesisData)
  } catch (error) {
    console.error('Error fetching pending theses for Fenix:', error)
  } finally {
    loading.value = false
  }
}

async function submitToFenix(thesis: ThesisDto) {
  try {
    await RemoteService.submitThesisToFenix(thesis.id)
    getPendingTheses()
  } catch (error) {
    console.error('Error submitting thesis to Fenix:', error)
  }
}
</script>
