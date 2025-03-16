import axios from 'axios'
import type { AxiosResponse } from 'axios'
import { useAppearanceStore } from '@/stores/appearance'
import DeiError from '@/models/DeiError'
import type PersonDto from '@/models/PersonDto'
import type ThesisWorkflowDto from '@/models/ThesisWorkflowDto'
import type ThesisDocumentDto from '@/models/ThesisDocumentDto'
import DefenseWorkflowDto, { DefenseState } from '@/models/DefenseWorkflowDto'

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

  static async getPerson(id: number): Promise<PersonDto> {
    return httpClient.get(`/people/${id}`)
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

  static async getDefenseByStudent(studentId: number): Promise<AxiosResponse<number>> {
    if (!studentId) {
      studentId = defaultData.studentId
    }
    return httpClient.get(`/defense/student/${studentId}`)
  }

  static async getDefenseById(defenseId: number): Promise<AxiosResponse<number>> {
    return httpClient.get(`/defense/${defenseId}`)
  }

  static async createThesis(studentId: number): Promise<AxiosResponse<number>> {
    return httpClient.post('/thesis', studentId)
  }

  static async createDefense(studentId: number): Promise<AxiosResponse<number>> {
    return httpClient.post('/defense', studentId)
  }

  static async getThesisProposals(): Promise<AxiosResponse<ThesisWorkflowDto[]>> {
    return httpClient.get('/thesis/proposal')
  }

  static async getThesisApproved(): Promise<AxiosResponse<ThesisWorkflowDto[]>> {
    return httpClient.get('/thesis/approved')
  }

  static async getThesisNotStarted(): Promise<AxiosResponse<ThesisWorkflowDto[]>> {
    return httpClient.get('/thesis/not-started')
  }

  static async getThesisPresidentAssigned(): Promise<AxiosResponse<ThesisWorkflowDto[]>> {
    return httpClient.get('/thesis/president-assigned')
  }

  static async getThesisDocumentSigned(): Promise<AxiosResponse<ThesisWorkflowDto[]>> {
    return httpClient.get('/thesis/document-signed')
  }

  static async getThesisFenixSubmitted(): Promise<AxiosResponse<ThesisWorkflowDto[]>> {
    return httpClient.get('/thesis/fenix-submitted')
  }

  static async deleteThesisWorkflow(id: number): Promise<AxiosResponse<void>> {
    return httpClient.delete(`/thesis/${id}`)
  }

  static async setThesisState(
    id: number,
    state: string
  ): Promise<AxiosResponse<ThesisWorkflowDto>> {
    return httpClient.post(`/thesis/${id}/set-state/${state}`)
  }

  static async submitThesisProposal(
    id: number,
    juryIds: number[]
  ): Promise<AxiosResponse<ThesisWorkflowDto>> {
    return httpClient.post(`/thesis/${id}/submit-proposal`, juryIds)
  }

  static async approveThesisProposal(id: number): Promise<AxiosResponse<ThesisWorkflowDto>> {
    return httpClient.post(`/thesis/${id}/approve-proposal`)
  }

  static async assignThesisPresident(
    id: number,
    presidentId: number
  ): Promise<AxiosResponse<ThesisWorkflowDto>> {
    return httpClient.post(`/thesis/${id}/assign-president/${presidentId}`)
  }

  static async signThesisDocument(
    id: number,
    formData: FormData
  ): Promise<AxiosResponse<ThesisWorkflowDto>> {
    return httpClient.post(`/thesis/${id}/sign-document`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  }

  static async scheduleDefense(
    id: number,
    date: string
  ): Promise<AxiosResponse<DefenseWorkflowDto>> {
    return httpClient.post(`/defense/${id}/schedule`, date)
  }

  static async markDefenseUnderReview(id: number): Promise<AxiosResponse<DefenseWorkflowDto>> {
    return httpClient.post(`/defense/${id}/mark-under-review`)
  }

  static async submitDefenseGrade(
    id: number,
    grade: number
  ): Promise<AxiosResponse<DefenseWorkflowDto>> {
    return httpClient.post(`/defense/${id}/submit-grade`, grade)
  }

  static async submitThesisToFenix(id: number): Promise<AxiosResponse<ThesisWorkflowDto>> {
    return httpClient.post(`/thesis/${id}/submit-fenix`)
  }

  static async defenseSetState(
    id: number,
    state: DefenseState
  ): Promise<AxiosResponse<DefenseWorkflowDto>> {
    return httpClient.post(`/defense/${id}/set-state`, state)
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
