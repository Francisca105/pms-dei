<template>
  <v-row align="center">
    <v-col>
      <h2 class="text-left ml-1">Atribuir Presidente do Júri para Propostas Pendentes</h2>
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
    :items="pendingProposals"
    :search="search"
    :loading="loading"
    item-key="id"
    class="text-left"
    no-data-text="Sem propostas pendentes."
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
    <template v-slot:item.actions="{ item }">
      <v-btn color="primary" @click="openModal(item)">Atribuir Presidente</v-btn>
    </template>
  </v-data-table>

  <v-dialog v-model="modalOpen" max-width="600">
    <v-card>
      <v-card-title>
        <span class="headline">Atribuir Presidente do Júri</span>
      </v-card-title>
      <v-card-text>
        <v-select
          v-model="selectedPresident"
          :items="currentProposal?.jury || []"
          item-title="name"
          item-value="id"
          label="Selecione o Presidente do Júri"
          outlined
        ></v-select>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="error" @click="closeModal">Cancelar</v-btn>
        <v-btn color="primary" @click="assignPresident">Confirmar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import type ThesisWorkflowDto from '@/models/ThesisWorkflowDto'
import RemoteService from '@/services/RemoteService'
import { reactive, ref } from 'vue'

const search = ref('')
const loading = ref(true)
const modalOpen = ref(false)
const selectedPresident = ref(null)
const currentProposal = ref<ThesisWorkflowDto | null>(null)

const headers = [
  { title: 'ID', key: 'id', sortable: true, filterable: false },
  {
    title: 'Estudante',
    key: 'student',
    sortable: true,
    filterable: true
  },
  {
    title: 'Júri Proposto',
    key: 'jury',
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

const pendingProposals = reactive<ThesisWorkflowDto[]>([])

getPendingProposals()
async function getPendingProposals() {
  try {
    const proposalData = await RemoteService.getThesisApproved()
    pendingProposals.splice(0, pendingProposals.length)
    pendingProposals.push(...proposalData)
  } catch (error) {
    console.error('Error fetching pending proposals:', error)
  } finally {
    loading.value = false
  }
}

function openModal(proposal: ThesisWorkflowDto) {
  currentProposal.value = proposal
  modalOpen.value = true
}

function closeModal() {
  modalOpen.value = false
  selectedPresident.value = null
  currentProposal.value = null
}

async function assignPresident() {
  if (!currentProposal.value || !selectedPresident.value) return

  try {
    await RemoteService.assignThesisPresident(currentProposal.value.id, selectedPresident.value)
    closeModal()
    await getPendingProposals()
  } catch (error) {
    console.error('Error assigning jury president:', error)
  }
}
</script>
