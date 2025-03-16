<template>
  <div v-if="roleStore.currentRole === 'staff'">
    <ThesisAdminForm />
  </div>
  <div v-else>
    <Error403 />
  </div>
</template>

<script setup lang="ts">
import { useRoleStore } from '@/stores/role'
import Error403 from '@/components/errors/Error403.vue'
import ThesisAdminForm from './ThesisAdminForm.vue'

const roleStore = useRoleStore()
</script>
