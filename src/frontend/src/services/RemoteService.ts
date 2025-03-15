import axios from 'axios'
import type { AxiosResponse } from 'axios'
import { useAppearanceStore } from '@/stores/appearance'
import DeiError from '@/models/DeiError'
import type PersonDto from '@/models/PersonDto'

const httpClient = axios.create()
httpClient.defaults.timeout = 50000
httpClient.defaults.baseURL = import.meta.env.VITE_ROOT_API
httpClient.defaults.headers.post['Content-Type'] = 'application/json'

export default class RemoteServices {
  static async getPeople(): Promise<PersonDto[]> {
    return httpClient.get('/people')
  }

  static async getStudents(): Promise<PersonDto[]> {
    return httpClient.get('/students')
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
