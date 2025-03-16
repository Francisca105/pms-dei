<template>
  <v-container>
    <v-stepper v-model="currentStep" :items="steps">
      <template v-slot:item.1>
        <v-card title="Defesa Agendada" flat>
          <v-card-text>
            <p>Agende a data e hora da defesa:</p>
            <v-date-picker v-model="defenseDate" :min="minDate" :max="maxDate" />
            <v-btn
              @click="scheduleDefense"
              color="primary"
              :disabled="!defenseDate"
            >
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
          <v-btn
            @click="onPrev(prev)"
            :disabled="currentStep === 1"
          >
            Voltar
          </v-btn>

          <v-btn
            @click="onNext(next)"
            :disabled="isNextDisabled"
            color="primary"
          >
            {{ nextButtonText }}
          </v-btn>
        </div>
      </template>
    </v-stepper>
  </v-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import RemoteService from '@/services/RemoteService'
import { useRoute } from 'vue-router'
import { DefenseState } from '../../../models/DefenseWorkflowDto'

const route = useRoute()
const defenseId = route.params.id
/*

  static async scheduleDefense(id: number, date: string): Promise<AxiosResponse<DefenseWorkflowDto>> {
    return httpClient.post(`/defense/${id}/schedule`, { date })
  }

  static async markDefenseUnderReview(id: number): Promise<AxiosResponse<DefenseWorkflowDto>> {
    return httpClient.post(`/defense/${id}/mark-under-review`)
  }

  static async submitDefenseGrade(id: number, grade: number): Promise<AxiosResponse<DefenseWorkflowDto>> {
    return httpClient.post(`/defense/${id}/submit-grade`, { grade })
  }
*/

const currentStep = ref(1)
const steps = ['Defesa Agendada', 'Em Revisão', 'Submetido ao Fenix']
const defenseDate = ref(null)
const finalGrade = ref(null)

const isNextDisabled = computed(() => {
  switch (currentStep.value) {
    case 1: return !defenseDate.value
    case 2: return false
    case 3: return !isGradeValid.value
    default: return false
  }
})

const nextButtonText = computed(() => {
  switch (currentStep.value) {
    case 1: return 'Próximo (Coloca em Revisão)'
    case 2: return 'Próximo'
    case 3: return 'Finalizar'
    default: return 'Próximo'
  }
})

async function onNext(next) {
  try {
    switch (currentStep.value) {
      case 1:
        // await scheduleDefense()
        break
      case 2:
        // await startReview()
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
      // await RemoteService.defenseSetState(defenseId, DefenseState.NOT_SCHEDULED)
      break
    case 2:
      console.log('prev2')
      await RemoteService.defenseSetState(defenseId, DefenseState.NOT_SCHEDULED)
      break
    case 3:
      console.log('prev3')
      await RemoteService.defenseSetState(defenseId, DefenseState.SCHEDULED_DEFENSE)
      // await RemoteService.defenseSetState(defenseId, DefenseState.UNDER_REVIEW)
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
  if (defenseDate.value ) {
    const isoDate = defenseDate.value.toISOString().split('T')[0]

    try {
      await RemoteService.scheduleDefense(defenseId, isoDate)
      currentStep.value = 2
    } catch (error) {
      console.error('Error scheduling defense:', error)
    }
  } else {
    alert('Preencha data e hora!')
  }
}

async function startReview() {
  await RemoteService.setState(defenseId, DefenseState.UNDER_REVIEW)
}

async function submitToFenix() {
  await RemoteService.submitDefenseGrade(defenseId, parseFloat(finalGrade.value))
}

const minDate = new Date().toISOString().split('T')[0]
const maxDate = new Date(new Date().setFullYear(new Date().getFullYear() + 1))
  .toISOString()
  .split('T')[0]
</script>
