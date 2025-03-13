import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import PeopleView from '@/views/people/PeopleView.vue'
import StudenstView from '../views/students/StudentsView.vue'
import StatisticsView from '@/views/statistics/StatisticsView.vue'
import { useRoleStore } from '@/stores/role'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/people',
      name: 'people',
      component: PeopleView
    },
    {
      path: '/students',
      name: 'students',
      component: StudenstView,
      beforeEnter: (to, from, next) => {
        const roleStore = useRoleStore()
        console.log('roleStore', roleStore.currentActiveRole)
        if (roleStore.isStudent) {
          next('/403') // Redireciona para a página de erro
        } else {
          next()
        }
      }
    },
    {
      path: '/statistics',
      name: 'statistics',
      component: StatisticsView
    },

    {
      path: '/403',
      name: 'forbidden',
      component: () => import('@/views/errors/Error403.vue')
    },
    {
      path: '/:pathMatch(.*)*', // Wildcard
      name: 'not-found',
      component: () => import('@/views/errors/Error404.vue')
    }
  ]
})

export default router
