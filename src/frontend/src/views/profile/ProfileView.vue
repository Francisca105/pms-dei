<template>
  <div class="pa-4">
    <template v-if="student">
      <v-card class="mb-6">
        <v-card-title>
          <div>
            <h2>Perfil</h2>
          </div>
        </v-card-title>

        <v-card-text>
          <v-row>
            <v-col cols="12" sm="6">
              <p class="text-caption text-medium-emphasis">Nome</p>
              <p class="text-body-2">{{ student.name }}</p>
            </v-col>
            <v-col cols="12" sm="6">
              <p class="text-caption text-medium-emphasis">IST ID</p>
              <p class="text-body-2">{{ student.istId }}</p>
            </v-col>
            <v-col cols="12" sm="6">
              <p class="text-caption text-medium-emphasis">Email</p>
              <p class="text-body-2">{{ student.email }}</p>
            </v-col>
            <v-col cols="12" sm="6">
              <p class="text-caption text-medium-emphasis">Tipo</p>
              <p class="text-body-2">{{ student.type }}</p>
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>

      <template v-if="thesis">
        <v-card>
          <v-card-title>
            <div>
              <div>
                <h3 class="text-h6">Informação da Tese</h3>
                <p class="text-subtitle-2 text-white text-opacity-80">
                  Detalhes sobre a tese do estudante
                </p>
              </div>
            </div>
          </v-card-title>

          <v-card-text>
            <v-row>
              <v-col cols="12" sm="6">
                <p class="text-caption text-medium-emphasis">Estado</p>
                <v-chip :color="getStatusClass(thesis.state)" size="small" class="mt-1">
                  {{ formatStatus(thesis.state) }}
                </v-chip>
              </v-col>

              <v-col cols="12" sm="6">
                <p class="text-caption text-medium-emphasis">Documento Assinado</p>
                <p class="text-body-2">
                  {{ thesis.signedDocument ? 'Sim' : 'Não' }}
                </p>
              </v-col>
            </v-row>

            <v-row class="mt-2">
              <v-col cols="12">
                <p class="text-caption text-medium-emphasis">Júri</p>
                <v-list v-if="thesis.jury && thesis.jury.length > 0">
                  <v-list-item v-for="member in thesis.jury" :key="member.id">
                    <v-list-item-title class="text-body-2">{{ member.name }}</v-list-item-title>
                    <v-list-item-subtitle>{{ member.email }}</v-list-item-subtitle>
                  </v-list-item>
                </v-list>
                <p v-else class="text-body-2 text-medium-emphasis">
                  Os elementos do júri ainda não foram propostos
                </p>
              </v-col>
            </v-row>

            <v-row class="mt-2">
              <v-col cols="12">
                <p class="text-caption text-medium-emphasis">Presidente</p>
                <v-list v-if="thesis.president">
                  <v-list-item>
                    <template v-slot:prepend>
                      <v-avatar color="grey-lighten-3" size="36">
                        <v-icon color="grey">mdi-account-check</v-icon>
                      </v-avatar>
                    </template>
                    <v-list-item-title class="text-body-2">{{
                      thesis.president.name
                    }}</v-list-item-title>
                    <v-list-item-subtitle
                      >{{ thesis.president.type }} -
                      {{ thesis.president.email }}</v-list-item-subtitle
                    >
                  </v-list-item>
                </v-list>
                <p v-else class="text-body-2 text-medium-emphasis">
                  O presidente do júri ainda não foi escolhido
                </p>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </template>
    </template>

    <template v-else>
      <v-alert type="error" v-if="errorMessage">
        {{ errorMessage }}
      </v-alert>
      <v-progress-circular v-else indeterminate></v-progress-circular>
    </template>
  </div>
</template>

<script setup>
import { ref, watch, onBeforeMount } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import RemoteService from '@/services/RemoteService'

const route = useRoute()
const router = useRouter()
const studentId = ref(null)

const student = ref(null)
const thesis = ref(null)
const defense = ref(null)

onBeforeMount(async () => {
  if (route.params.id) {
    studentId.value = route.params.id
    await fetchData(studentId.value)
  } else {
    router.push('/404')
  }
})

watch(studentId, async (newId) => {
  if (newId) {
    await fetchData(newId)
  }
})

async function fetchData(id) {
  try {
    const studentResponse = await RemoteService.getPerson(id)
    student.value = studentResponse
  } catch (error) {
    student.value = null
    console.error('Erro ao carregar dados do estudante:', error)
  }

  if (!student.value) {
    router.push('/404')
    return
  }

  try {
    const thesisResponse = await RemoteService.getThesisByStudent(id)
    thesis.value = thesisResponse
  } catch (error) {
    thesis.value = null
    console.error('Erro ao carregar dados da tese:', error)
  }

  try {
    const defenseResponse = await RemoteService.getDefenseByStudent(id)
    defense.value = defenseResponse
  } catch (error) {
    defense.value = null
    console.error('Erro ao carregar dados da defesa:', error)
  }
}

const formatStatus = (status) => {
  return status
    .replace(/_/g, ' ')
    .toLowerCase()
    .replace(/\b\w/g, (char) => char.toUpperCase())
}

const getStatusClass = (status) => {
  const statusMap = {
    PROPOSAL_SUBMITTED: 'bg-blue-100 text-blue-800',
    APPROVED: 'bg-green-100 text-green-800',
    REJECTED: 'bg-red-100 text-red-800',
    IN_PROGRESS: 'bg-yellow-100 text-yellow-800',
    COMPLETED: 'bg-purple-100 text-purple-800'
  }

  return statusMap[status] || 'bg-gray-100 text-gray-800'
}
</script>
