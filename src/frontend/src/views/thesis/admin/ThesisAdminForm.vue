<template>
  <v-container>
    <v-card v-if="thesisDetails" class="mb-4" flat>
      <v-card-title>Detalhes da Tese</v-card-title>
      <v-card-text>
        <p><strong>ID da Tese:</strong> {{ thesisDetails.id }}</p>
        <p><strong>Nome do Estudante:</strong> {{ thesisDetails.student.name }}</p>
        <p><strong>Email do Estudante:</strong> {{ thesisDetails.student.email }}</p>
        <p><strong>Estado:</strong> {{ thesisDetails.state }}</p>

        <p>
          <strong>Presidente:</strong>
          {{
            thesisDetails.president ? thesisDetails.president.name : 'Nenhum presidente definido'
          }}
        </p>

        <p>
          <strong>Documento Assinado:</strong>
          {{
            thesisDetails.signedDocument
              ? thesisDetails.signedDocument.name
              : 'Nenhum documento assinado'
          }}
        </p>

        <p>
          <strong>Data de Upload:</strong>
          {{
            thesisDetails.signedDocument
              ? thesisDetails.signedDocument.uploadDate
              : 'Nenhuma data disponível'
          }}
        </p>

        <p>
          <strong>Júri:</strong>
          {{
            thesisDetails.jury && thesisDetails.jury.length > 0
              ? thesisDetails.jury.map((m) => m.name).join(', ')
              : 'Nenhum membro do júri definido'
          }}
        </p>
      </v-card-text>
    </v-card>

    <v-card class="mb-4" flat>
      <v-card-title>Alterar Estado da Tese</v-card-title>
      <v-card-text>
        <v-select
          v-model="selectedState"
          :items="states"
          label="Selecione o Estado"
          outlined
        ></v-select>
        <v-btn @click="updateState" color="primary" :disabled="!selectedState">
          Atualizar Estado
        </v-btn>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import RemoteService from '@/services/RemoteService'
import { useRoute } from 'vue-router'
import { ThesisState } from '@/models/ThesisWorkflowDto'

const route = useRoute()
const thesisId = route.params.id

const thesisDetails = ref(null)
const selectedState = ref(null)
const states = ref([
  ThesisState.NOT_STARTED,
  ThesisState.PROPOSAL_SUBMITTED,
  ThesisState.APPROVED_SC,
  ThesisState.PRESIDENT_ASSIGNED,
  ThesisState.DOCUMENT_SIGNED,
  ThesisState.SUBMITTED_FENIX
])

async function fetchThesisDetails() {
  try {
    const response = await RemoteService.getThesisById(thesisId)
    thesisDetails.value = response
    selectedState.value = thesisDetails.value.state
  } catch (error) {
    console.error('Erro ao buscar detalhes da tese:', error)
  }
}

async function updateState() {
  try {
    const response = await RemoteService.thesisSetState(thesisId, selectedState.value)
    thesisDetails.value = response
  } catch (error) {
    console.error('Erro ao atualizar o estado da tese:', error)
  }
}

onMounted(() => {
  fetchThesisDetails()
})
</script>
