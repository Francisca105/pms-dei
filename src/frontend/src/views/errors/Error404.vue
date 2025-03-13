<template>
  <v-container class="fill-height text-center">
    <v-row justify="center" align="center">
      <v-col cols="12" md="6">
        <v-icon size="100" color="grey darken-2">mdi-compass</v-icon>
        <h1 class="text-h4 font-weight-bold mt-4">Página Não Encontrada</h1>
        <p class="text-body-1 mt-2">A página que procuras não existe ou foi movida.</p>
        <v-btn color="primary" class="mt-4" @click="$router.push('/')">
          Ir para a Página Inicial
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.fill-height {
  height: 100vh;
}
</style>
