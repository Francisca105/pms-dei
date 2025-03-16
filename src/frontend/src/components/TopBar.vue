<template>
  <UtilBar />
  <NavBar :navbarItems="navbarItems" />
</template>

<script setup lang="ts">
import UtilBar from '@/components/UtilBar.vue'
import NavBar from '@/components/NavBar.vue'

const navbarItems = [
  {
    name: 'Tese',
    path: '/thesis',
    icon: 'mdi-book',
    children: [
      { name: 'Criar', path: '/thesis/create' },
      { name: 'Aprovar Juri', path: '/thesis/jury' },
      { name: 'Selecionar Presidente', path: '/thesis/jury/president' },
      { name: 'Assinar Documentos', path: '/thesis/sign/pending' },
      { name: 'Submeter Fenix', path: '/thesis/fenix' }
    ]
  },
  { name: 'Defesa', path: '/defense', icon: 'mdi-shield' },
  { name: 'Pessoal', path: '/people', icon: 'mdi-account-group' },
  { name: 'Students', path: '/students', icon: 'mdi-account-school' },
  { name: 'Estatísticas', path: '/statistics', icon: 'mdi-chart-bar' }
]
</script>
