<template>
  <v-container>
    <v-stepper v-model="currentStep" :items="steps">
      <template v-slot:item.1>
        <v-card title="Defesa Agendada" flat>
          <v-card-text>
            <p>Agende a data e hora da defesa:</p>
            <v-date-picker v-model="defenseDate" :min="minDate" :max="maxDate" />
            <v-text-field
              v-model="defenseTime"
              type="time"
              label="Hora da Defesa"
              :rules="[validateTime]"
            />
            <v-btn
              @click="scheduleDefense"
              color="primary"
              :disabled="!defenseDate || !defenseTime"
            >
              Agendar Defesa
            </v-btn>
          </v-card-text>
        </v-card>
      </template>

      <template v-slot:item.2>
        <v-card title="Em Revisão" flat>
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
            <v-btn @click="startReview" color="primary" :disabled="!isGradeValid">
              Iniciar Revisão
            </v-btn>
          </v-card-text>
        </v-card>
      </template>

      <template v-slot:item.3>
        <v-card title="Submetido ao Fenix" flat>
          <v-card-text>
            <p>A defesa foi concluída e a nota foi atribuída.</p>
            <v-btn @click="submitToFenix" color="primary">Submeter ao Fenix</v-btn>
          </v-card-text>
        </v-card>
      </template>
    </v-stepper>
  </v-container>
</template>

<script setup>
import { ref, computed } from 'vue'

const currentStep = ref(1)
const steps = ['Defesa Agendada', 'Em Revisão', 'Submetido ao Fenix']
const defenseDate = ref(null)
const defenseTime = ref(null)
const finalGrade = ref(null)

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

function scheduleDefense() {
  if (defenseDate.value && defenseTime.value) {
    const isoDate = defenseDate.value.toISOString().split('T')[0]
    const dateTimeString = `${isoDate}T${defenseTime.value}:00`

    const dateTime = new Date(dateTimeString)

    if (isNaN(dateTime.getTime())) {
      alert('Data/hora inválida! Verifique os valores.')
      return
    }

    if (dateTime < new Date()) {
      alert('Data/hora inválida! A data/hora deve ser futura.')
      return
    }

    const formattedDate = new Intl.DateTimeFormat('pt-PT', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    }).format(dateTime)

    const formattedTime = new Intl.DateTimeFormat('pt-PT', {
      hour: '2-digit',
      minute: '2-digit',
      hour12: false
    }).format(dateTime)

    alert(`Defesa agendada para ${formattedDate} às ${formattedTime}`)
    currentStep.value = 2
  } else {
    alert('Preencha data e hora!')
  }
}

function validateTime(value) {
  const timeRegex = /^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/
  if (!value || !timeRegex.test(value)) {
    return 'Hora inválida (formato HH:MM)'
  }
  return true
}

function startReview() {
  if (isGradeValid.value) {
    alert(`Nota ${finalGrade.value} atribuída. Iniciando revisão...`)
    currentStep.value = 3
  }
}

function submitToFenix() {
  alert('Defesa submetida ao Fenix com sucesso!')
}

const minDate = new Date().toISOString().split('T')[0]
const maxDate = new Date(new Date().setFullYear(new Date().getFullYear() + 1))
  .toISOString()
  .split('T')[0]
</script>
