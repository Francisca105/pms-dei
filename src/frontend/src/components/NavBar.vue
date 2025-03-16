<template>
  <v-app-bar>
    <v-spacer></v-spacer>
    <v-menu v-for="item in navbarItems" :key="item.name" offset-y>
      <template v-slot:activator="{ props }">
        <v-btn v-bind="props" :to="item.path">
          {{ item.name }}
          <v-icon color="contrast" :icon="item.icon"></v-icon>
        </v-btn>
      </template>
      <v-list v-if="item.children && item.children.length">
        <v-list-item v-for="child in item.children" :key="child.name" :to="child.path">
          {{ child.name }}
        </v-list-item>
      </v-list>
    </v-menu>
  </v-app-bar>
</template>

<script setup lang="ts">
defineProps<{
  navbarItems: {
    name: string
    path: string
    icon: string
    children?: { name: string; path: string }[]
  }[]
}>()
</script>
