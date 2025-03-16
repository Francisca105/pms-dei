<template>
  <div class="text-center">
    <h1>Teses</h1>

    <br />

    <div v-if="roleStore.currentRole === 'coordinator'">
      <h2>Área do Coordenador</h2>
      <p>Aqui pode definir presidentes de juri e assinar documentos.</p>
      <router-link to="/thesis/jury/president">Gerir Júris</router-link>
      <router-link to="/thesis/sign/pending">Assinar documentos</router-link>
    </div>

    <div v-else-if="roleStore.currentRole === 'staff'">
      <h2>Área do Staff</h2>
      <p>Aqui pode submeter documentos ao Fenix.</p>
      <router-link to="/thesis/fenix">Submeter no Fenix</router-link>
    </div>

    <div v-else-if="roleStore.currentRole === 'admin'">
      <h2>Área do SC (Scientific Committee)</h2>
      <p>Aqui pode aprovar propostas de júri.</p>
      <router-link to="/thesis/jury">Ver Propostas</router-link>
    </div>

    <div v-else>
      <h2>Área Geral</h2>
      <p>Aqui pode ver estatísticas e relatórios.</p>
      <router-link to="/statistics">Ver Estatísticas</router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRoleStore } from '@/stores/role'
import RemoteService from '@/services/RemoteService'
import ThesisWorkflowDto from '@/models/ThesisWorkflowDto'
import { reactive, ref } from 'vue'

let thesis: ThesisWorkflowDto[] = reactive([])

getThesis()
async function getThesis() {
  thesis.splice(0, thesis.length)
  thesis.push(...(await RemoteService.getThesis()))
}

const roleStore = useRoleStore()
</script>
