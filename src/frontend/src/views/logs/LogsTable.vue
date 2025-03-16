<template>
    <v-row align="center">
      <v-col>
        <h2 class="text-left ml-1">Listagem de Logs</h2>
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
      :items="logs"
      :search="search"
      :loading="loading"
      :custom-filter="fuzzySearch"
      item-key="id"
      class="text-left"
      no-data-text="Sem logs a apresentar."
    >
      <template v-slot:[`item.logType`]="{ item }">
        <v-chip :color="getLogTypeColor(item.logType)" text-color="white">
          {{ item.logType }}
        </v-chip>
      </template>
    </v-data-table>
  </template>

<script setup lang="ts">
import type LogDto from '@/models/LogDto'
import RemoteService from '@/services/RemoteService'
import { reactive, ref } from 'vue'

let search = ref('')
let loading = ref(true)

const headers = [
  { title: 'ID', key: 'id', value: 'id', sortable: true, filterable: false },
  {
    title: 'Tipo',
    key: 'logType',
    value: 'logType',
    sortable: true,
    filterable: true
  },
  {
    title: 'Data e Hora',
    key: 'timestamp',
    value: 'timestamp',
    sortable: true,
    filterable: true
  },
  {
    title: 'Mensagem',
    key: 'message',
    value: 'message',
    sortable: true,
    filterable: true
  }
]

let logs: LogDto[] = reactive([])

getLogs()
async function getLogs() {
  logs.splice(0, logs.length)
  logs.push(...(await RemoteService.getLogs()))
  loading.value = false
}

const getLogTypeColor = (logType: string) => {
  switch (logType) {
    case 'INFO':
      return 'blue'
    case 'ERROR':
      return 'red'
    case 'WARNING':
      return 'orange'
    default:
      return 'green'
  }
}

const fuzzySearch = (value: string, search: string) => {
  if (!value) return false
  let searchRegex = new RegExp(search.split('').join('.*'), 'i')
  return searchRegex.test(value)
}
</script>