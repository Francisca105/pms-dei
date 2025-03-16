<template>
  <v-row align="center">
    <v-col>
      <h2 class="text-left ml-1">Propostas de Juri Pendentes</h2>
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
    :items="proposals"
    :search="search"
    :loading="loading"
    :custom-filter="fuzzySearch"
    item-key="id"
    class="text-left"
    no-data-text="Sem propostas a apresentar."
  >
    <template v-slot:item.actions="{ item }">
      <v-btn color="success" @click="approveProposal(item)">Aprovar</v-btn>
      <v-btn color="error" @click="rejectProposal(item)">Rejeitar</v-btn>
    </template>
  </v-data-table>
</template>

<script setup lang="ts">
import ThesisWorkflowDto, { ThesisState } from '@/models/ThesisWorkflowDto'
import RemoteService from '@/services/RemoteService'
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useRoleStore } from '@/stores/role'

const roleStore = useRoleStore()
const route = useRoute()
const router = useRouter()
const search = ref('')
const loading = ref(true)

const headers = [
  { title: 'ID', key: 'id', sortable: true, filterable: false },
  {
    title: 'Estudante',
    key: 'student.name',
    sortable: true,
    filterable: true
  },
  {
    title: 'Professores Propostos',
    key: 'jury',
    sortable: true,
    filterable: true
  },
  {
    title: 'Estado',
    key: 'state',
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

const proposals = reactive<ThesisWorkflowDto[]>([])

getProposals()
async function getProposals() {
  try {
    const proposalData = await RemoteService.getThesisProposals()
    proposals.splice(0, proposals.length)
    proposals.push(...proposalData)
  } catch (error) {
    console.error('Error fetching proposals:', error)
  } finally {
    loading.value = false
  }
}

const fuzzySearch = (value: string, search: string) => {
  if (!value || !search) return true
  const searchRegex = new RegExp(search.split('').join('.*'), 'i')
  return searchRegex.test(value)
}

async function approveProposal(proposal: ThesisWorkflowDto) {
  try {
    await RemoteService.approveThesisProposal(proposal.id)
    proposal.state = 'Aprovado pelo SC'
    roleStore.currentRole = 'coordinator'
    router.push(`/thesis/jury/president`)
  } catch (error) {
    console.error('Error approving proposal:', error)
  }
}

async function rejectProposal(proposal: ThesisWorkflowDto) {
  try {
    await RemoteService.setThesisState(proposal.id, ThesisState.NOT_STARTED)
    proposal.state = 'Rejeitado pelo SC'
  } catch (error) {
    console.error('Error rejecting proposal:', error)
  }
}
</script>
