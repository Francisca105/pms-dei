<template>
  <div class="text-center">
    <h1>Bem-vindo ao PMS!</h1>
    <p>Página inicial para {{ appName }}</p>
    <br />

    <div v-if="roleStore.currentRole === 'student'">
      <h2>Área do Estudante</h2>
      <p>Aqui pode gerir a sua tese e defesa.</p>
      <router-link to="/profile/1">Ver o Meu Progresso</router-link>
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

const appName = import.meta.env.VITE_NAME
const roleStore = useRoleStore()
</script>
