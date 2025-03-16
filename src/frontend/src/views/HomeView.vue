<template>
  <div class="text-center">
    <h1>Bem-vindo ao PMS!</h1>
    <p>Página inicial para {{ appName }}</p>
    <br />

    <!-- Role-based content -->
    <div v-if="roleStore.currentRole === 'student'">
      <h2>Área do Estudante</h2>
      <p>Aqui pode gerir a sua tese e defesa.</p>
      <router-link to="/profile/1">Ver o Meu Progresso</router-link>
    </div>

    <div v-else-if="roleStore.currentRole === 'coordinator'">
      <h2>Área do Coordenador</h2>
      <p>Aqui pode aprovar propostas, agendar defesas e gerir workflows.</p>
      <router-link to="/students">Gerir Estudantes</router-link>
    </div>

    <div v-else-if="roleStore.currentRole === 'staff'">
      <h2>Área do Staff</h2>
      <p>Aqui pode submeter documentos ao Fenix.</p>
      <router-link to="/students">Ver Estudantes</router-link>
    </div>

    <div v-else-if="roleStore.currentRole === 'admin'">
      <h2>Área do SC (Scientific Committee)</h2>
      <p>Aqui pode aprovar propostas de júri.</p>
      <router-link to="/students">Ver Propostas</router-link>
    </div>

    <div v-else-if="roleStore.currentRole === 'teacher'">
      <h2>Área do Professor</h2>
      <p>Aqui pode participar em júris e revisões.</p>
      <router-link to="/students">Ver Júris</router-link>
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
