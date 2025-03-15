import PersonDto from './PersonDto'
import ThesisDocumentDto from './ThesisDocumentDto'

export enum ThesisState {
  PROPOSAL_SUBMITTED = 'PROPOSAL_SUBMITTED',
  APPROVED_SC = 'APPROVED_SC',
  PRESIDENT_ASSIGNED = 'PRESIDENT_ASSIGNED',
  DOCUMENT_SIGNED = 'DOCUMENT_SIGNED',
  SUBMITTED_FENIX = 'SUBMITTED_FENIX'
}

export default class ThesisWorkflowDto {
  id?: number
  student?: PersonDto
  state?: ThesisState
  jury?: PersonDto[]
  president?: PersonDto
  signedDocument?: ThesisDocumentDto

  constructor(obj?: Partial<ThesisWorkflowDto>) {
    Object.assign(this, obj)
  }
}
