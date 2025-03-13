<template>
  <v-container class="fill-height text-center">
    <v-row justify="center" align="center">
      <v-col cols="12" md="6">
        <v-icon size="100" color="red">mdi-lock</v-icon>
        <h1 class="text-h4 font-weight-bold mt-4">Acesso Negado</h1>
        <p class="text-body-1 mt-2">Não tens permissão para visualizar esta página.</p>
        <v-btn color="primary" class="mt-4" @click="$router.push('/')"> Voltar ao Início </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.fill-height {
  height: 100vh;
}
</style>
