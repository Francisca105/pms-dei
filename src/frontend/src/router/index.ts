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
      component: StudenstView
    },
    {
      path: '/statistics',
      name: 'statistics',
      component: StatisticsView
    },
    {
      path: '/thesis',
      name: 'thesis',
      component: () => import('@/views/thesis/ThesisView.vue')
    },
    {
      path: '/thesis/create',
      name: 'thesis-create',
      component: () => import('@/views/thesis/create/ThesisCreateView.vue')
    },
    {
      path: '/thesis/:id/jury',
      component: () => import('@/views/thesis/jury_selection/ThesisJurySelectionView.vue')
    },
    {
      path: '/403',
      name: 'forbidden',
      component: () => import('@/components/errors/Error403.vue')
    },
    {
      path: '/:pathMatch(.*)*', // Wildcard
      name: 'not-found',
      component: () => import('@/components/errors/Error404.vue')
    }
  ]
})

export default router
