import axios from 'axios'
import type { AxiosResponse } from 'axios'
import { useAppearanceStore } from '@/stores/appearance'
import DeiError from '@/models/DeiError'
import type PersonDto from '@/models/PersonDto'

const httpClient = axios.create()
httpClient.defaults.timeout = 50000
httpClient.defaults.baseURL = import.meta.env.VITE_ROOT_API
httpClient.defaults.headers.post['Content-Type'] = 'application/json'

// TODO: Change this when I have the final populate.sql
const defaultData = {
  studentId: 1,
  teacherId: 3,
  coordinatorId: 0,
  staffId: 0,
  scId: 0
}

export default class RemoteServices {
  static async getPeople(): Promise<PersonDto[]> {
    return httpClient.get('/people')
  }

  static async getStudents(): Promise<PersonDto[]> {
    return httpClient.get('/students')
  }

  static async getTeachers(): Promise<PersonDto[]> {
    return httpClient.get('/teachers')
  }

  static async createPerson(person: PersonDto): Promise<PersonDto> {
    return httpClient.post('/people', person)
  }

  static async updatePerson(person: PersonDto): Promise<PersonDto> {
    return httpClient.put(`/people/${person.id}`, person)
  }

  static async deletePerson(id: number): Promise<AxiosResponse<void>> {
    return httpClient.delete(`/people/${id}`)
  }

  static async getThesisStatistics(): Promise<AxiosResponse<number>> {
    return httpClient.get('/thesis/statistics')
  }

  static async getDefenseStatistics(): Promise<AxiosResponse<number>> {
    return httpClient.get('/defense/statistics')
  }

  static async getThesis(): Promise<AxiosResponse<number>> {
    return httpClient.get('/thesis')
  }

  static async getDefense(): Promise<AxiosResponse<number>> {
    return httpClient.get('/defense')
  }

  static async getThesisByStudent(studentId: number): Promise<AxiosResponse<number>> {
    if (!studentId) {
      studentId = defaultData.studentId
    }
    return httpClient.get(`/thesis/student/${studentId}`)
  }

  static async createThesis(studentId: number): Promise<AxiosResponse<number>> {
    return httpClient.post('/thesis', studentId)
  }

  static async createDefense(studentId: number): Promise<AxiosResponse<number>> {
    console.log(studentId)
    return httpClient.post('/defense', studentId)
  }

  static async submitThesisProposal(
    thesisId: number,
    juryIds: number[]
  ): Promise<AxiosResponse<void>> {
    return httpClient.post(`/thesis/${thesisId}/submit-proposal`, juryIds)
  }

  static async errorMessage(error: any): Promise<string> {
    if (error.message === 'Network Error') {
      return 'Unable to connect to the server'
    } else if (error.message.split(' ')[0] === 'timeout') {
      return 'Request timeout - Server took too long to respond'
    } else {
      return error.response?.data?.message ?? 'Unknown Error'
    }
  }

  static async handleError(error: any): Promise<never> {
    const deiErr = new DeiError(
      await RemoteServices.errorMessage(error),
      error.response?.data?.code ?? -1
    )
    const appearance = useAppearanceStore()
    appearance.pushError(deiErr)
    appearance.loading = false
    throw deiErr
  }
}

httpClient.interceptors.request.use((request) => request, RemoteServices.handleError)
httpClient.interceptors.response.use((response) => response.data, RemoteServices.handleError)
