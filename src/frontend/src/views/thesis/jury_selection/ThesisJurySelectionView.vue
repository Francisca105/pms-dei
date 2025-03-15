<template>
  <div
    v-if="
      roleStore.currentRole === 'student' ||
      roleStore.currentRole === 'staff' ||
      roleStore.currentRole === 'admin'
    "
  >
    <TeachersTable />
  </div>
  <div v-else>
    <Error403 />
  </div>
</template>

<script setup lang="ts">
import { useRoleStore } from '@/stores/role'
import Error403 from '@/components/errors/Error403.vue'
import TeachersTable from './ThesisJurySelectionTable.vue'

const roleStore = useRoleStore()
</script>
