<!-- This is just a mockup of the StatisticsView.vue component -->
<!-- It is not meant to be your final implementation -->
<!-- You should replace this file with your own implementation -->
<!-- You are free to explore the available javascript libraries for charts and graphs -->
<!-- Some popular libraries are Chart.js, D3.js, vue-chartjs, ... -->
<!-- You can also use Vuetify components to enhance the visual appeal of your statistics -->

<template>
  <v-container>
    <v-row class="mb-6">
      <v-col cols="12">
        <h1 class="text-h4 text-center mb-4">PhD Thesis and Defenses Statistics</h1>
      </v-col>
    </v-row>

    <!-- Thesis Progress Section -->
    <v-row class="mb-6">
      <v-col cols="12">
        <h2 class="text-h5 mb-4">Thesis Progress</h2>
        <v-row>
          <v-col v-for="(count, step) in thesisProgress" :key="step" cols="12" sm="6" md="4" lg="3">
            <v-card class="stat-card">
              <v-card-text class="text-center">
                <h3 class="text-h6">{{ step }}</h3>
                <p class="text-h4 font-weight-bold">{{ count }}</p>
                <p class="text-caption">students</p>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-col>
    </v-row>

    <!-- Defense Progress Section -->
    <v-row class="mb-6">
      <v-col cols="12">
        <h2 class="text-h5 mb-4">Defense Progress</h2>
        <v-row>
          <v-col
            v-for="(count, step) in defenseProgress"
            :key="step"
            cols="12"
            sm="6"
            md="4"
            lg="3"
          >
            <v-card class="stat-card">
              <v-card-text class="text-center">
                <h3 class="text-h6">{{ step }}</h3>
                <p class="text-h4 font-weight-bold">{{ count }}</p>
                <p class="text-caption">students</p>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ThesisState } from '@/models/ThesisWorkflowDto'
import { DefenseState } from '@/models/DefenseWorkflowDto'
import RemoteService from '@/services/RemoteService'

const thesisProgress = ref({})
const defenseProgress = ref({})

const stateMapping_Thesis = {
  [ThesisState.PROPOSAL_SUBMITTED]: 'Jury Proposal Submitted',
  [ThesisState.APPROVED_SC]: 'Approved by SC',
  [ThesisState.PRESIDENT_ASSIGNED]: 'Jury President Assigned',
  [ThesisState.DOCUMENT_SIGNED]: 'Document Signed',
  [ThesisState.SUBMITTED_FENIX]: 'Submitted to Fenix',
  [ThesisState.NOT_STARTED]: 'Not Started'
}

const stateMapping_Defense = {
  [DefenseState.SCHEDULED_DEFENSE]: 'Defense Scheduled',
  [DefenseState.UNDER_REVIEW]: 'Defense Under Review',
  [DefenseState.SUBMITTED_FENIX]: 'Submitted to Fenix'
}

onMounted(async () => {
  // Fetch thesis statistics
  try {
    const data = await RemoteService.getThesisStatistics()
    const mappedData = {}

    for (const [state, count] of Object.entries(data)) {
      const label = stateMapping_Thesis[state]
      if (label) {
        mappedData[label] = count
      }
    }

    thesisProgress.value = mappedData
  } catch (error) {
    console.error('Failed to fetch thesis statistics:', error)
  }

  // Fetch defense statistics
  try {
    const data = await RemoteService.getDefenseStatistics()
    const mappedData = {}

    for (const [state, count] of Object.entries(data)) {
      const label = stateMapping_Defense[state]
      if (label) {
        mappedData[label] = count
      }
    }

    defenseProgress.value = mappedData
  } catch (error) {
    console.error('Failed to fetch thesis statistics:', error)
  }
})
</script>

<style scoped>
.stat-card {
  transition: transform 0.2s ease-in-out;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
</style>
