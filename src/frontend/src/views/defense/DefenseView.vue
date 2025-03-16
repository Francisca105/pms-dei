<template>
  <div class="text-center">
    <h1>Defesa</h1>

    <br />

    <div v-if="roleStore.currentRole === 'coordinator'">
      <h2>Área do Coordenador</h2>
      <router-link to="/students"
        >A gestão de uma defesa é feita de forma singular, entra numa defesa de um estudante e pode
        editar a partir daí.</router-link
      >
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

const roleStore = useRoleStore()
</script>
