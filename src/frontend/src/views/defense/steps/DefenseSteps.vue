<template>
  <v-container>
    <!-- Cartão fixo com os detalhes da tese -->
    <v-card v-if="defenseDetails" class="mb-4" flat>
      <v-card-title>Detalhes da Tese</v-card-title>
      <v-card-text>
        <p><strong>ID da Defesa:</strong> {{ defenseDetails.id }}</p>
        <p><strong>ID do Workflow da Tese:</strong> {{ defenseDetails.thesisWorkflowId }}</p>
        <p><strong>Estado:</strong> {{ defenseDetails.state }}</p>
        <p><strong>Data Agendada:</strong> {{ defenseDetails.scheduledDate }}</p>
        <p><strong>Nota:</strong> {{ defenseDetails.grade }}</p>
      </v-card-text>
    </v-card>

    <v-stepper v-model="currentStep" :items="steps">
      <template v-slot:item.1>
        <v-card title="Defesa Agendada" flat>
          <v-card-text>
            <p>Agende a data e hora da defesa:</p>
            <v-date-picker v-model="defenseDate" :min="minDate" :max="maxDate" />
            <v-btn @click="scheduleDefense" color="primary" :disabled="!defenseDate">
              Agendar Defesa
            </v-btn>
          </v-card-text>
        </v-card>
      </template>

      <template v-slot:item.2>
        <v-card title="Em Revisão" flat>
          <v-card-text>
            <p>A defesa está em revisão.</p>
          </v-card-text>
        </v-card>
      </template>

      <template v-slot:item.3>
        <v-card title="Submetido ao Fenix" flat>
          <v-card-text>
            <p>Insira a nota final do aluno:</p>
            <v-text-field
              v-model="finalGrade"
              type="number"
              label="Nota Final"
              :rules="[validateGrade]"
              min="0"
              max="20"
              step="0.1"
            />
            <v-btn @click="submitToFenix" color="primary">Submeter ao Fenix</v-btn>
          </v-card-text>
        </v-card>
      </template>

      <template v-slot:actions="{ prev, next }">
        <div class="d-flex justify-space-between">
          <v-btn @click="onPrev(prev)" :disabled="currentStep === 1"> Voltar </v-btn>

          <v-btn @click="onNext(next)" :disabled="isNextDisabled" color="primary">
            {{ nextButtonText }}
          </v-btn>
        </div>
      </template>
    </v-stepper>
  </v-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import RemoteService from '@/services/RemoteService'
import { useRoute } from 'vue-router'
import { DefenseState } from '../../../models/DefenseWorkflowDto'

const route = useRoute()
const defenseId = route.params.id

const currentStep = ref(1)
const steps = ['Defesa Agendada', 'Em Revisão', 'Submetido ao Fenix']
const defenseDate = ref(null)
const finalGrade = ref(null)
const defenseDetails = ref(null)

const isNextDisabled = computed(() => {
  switch (currentStep.value) {
    case 1:
      return false
    case 2:
      return false
    case 3:
      return !isGradeValid.value
    default:
      return false
  }
})

const nextButtonText = computed(() => {
  switch (currentStep.value) {
    case 1:
      return 'Próximo (Coloca em Revisão)'
    case 2:
      return 'Próximo'
    case 3:
      return 'Finalizar'
    default:
      return 'Próximo'
  }
})

async function onNext(next) {
  try {
    switch (currentStep.value) {
      case 1:
        await scheduleDefense()
        break
      case 2:
        await startReview()
        break
      case 3:
        await submitToFenix()
        break
    }
    next() // Só avança se a ação for bem sucedida
  } catch (error) {
    console.error('Erro na transição:', error)
    alert('Erro ao processar a ação')
  }
}

async function onPrev(prev) {
  switch (currentStep.value) {
    case 1:
      console.log('prev1')
      let response1 = await RemoteService.defenseSetState(defenseId, DefenseState.NOT_SCHEDULED)
      console.log(response1)
      break
    case 2:
      console.log('prev2')
      let response2 = await RemoteService.defenseSetState(defenseId, DefenseState.NOT_SCHEDULED)
      console.log(response2)
      break
    case 3:
      console.log('prev3')
      let response3 = await RemoteService.defenseSetState(defenseId, DefenseState.UNDER_REVIEW)
      console.log(response3)
      break
  }
  prev()
}

const isGradeValid = computed(() => {
  const grade = parseFloat(finalGrade.value)
  return !isNaN(grade) && grade >= 0 && grade <= 20
})

function validateGrade(value) {
  const grade = parseFloat(value)
  if (isNaN(grade) || grade < 0 || grade > 20) {
    return 'A nota deve estar entre 0 e 20.'
  }
  return true
}

async function scheduleDefense() {
  if (defenseDate.value) {
    const isoDate = defenseDate.value.toISOString().split('T')[0]

    try {
      let response = await RemoteService.scheduleDefense(defenseId, isoDate)
      defenseDetails.value = response
      console.log('Defesa agendada:', response)
      currentStep.value = 2
    } catch (error) {
      console.error('Error scheduling defense:', error)
    }
  }
}

async function startReview() {
  let response = await RemoteService.defenseSetState(defenseId, DefenseState.UNDER_REVIEW)
  defenseDetails.value = response
  console.log('Defesa em revisão:', response)
}

async function submitToFenix() {
  let response = await RemoteService.submitDefenseGrade(defenseId, parseFloat(finalGrade.value))
  defenseDetails.value = response
  console.log('Defesa submetida ao Fenix:', response)
}

const minDate = new Date().toISOString().split('T')[0]
const maxDate = new Date(new Date().setFullYear(new Date().getFullYear() + 1))
  .toISOString()
  .split('T')[0]

// Função para buscar os detalhes da defesa
async function fetchDefenseDetails() {
  try {
    const response = await RemoteService.getDefenseById(defenseId)
    defenseDetails.value = response
    console.log('Detalhes da defesa:', response)
    // Atualiza o estado atual com base no estado da defesa
    switch (defenseDetails.value.state) {
      case DefenseState.SCHEDULED_DEFENSE:
        currentStep.value = 1
        break
      case DefenseState.UNDER_REVIEW:
        currentStep.value = 2
        break
      case DefenseState.SUBMITTED_FENIX:
        currentStep.value = 3
        break
      default:
        currentStep.value = 1
    }
  } catch (error) {
    console.error('Erro ao buscar detalhes da defesa:', error)
  }
}

// Busca os detalhes da defesa ao carregar a página
onMounted(() => {
  fetchDefenseDetails()
})
</script>
