import { LocalDate } from "@/types";

export enum DefenseState {
  NOT_SCHEDULED = "NOT_SCHEDULED",
  SCHEDULED_DEFENSE = "SCHEDULED_DEFENSE",
  UNDER_REVIEW = "UNDER_REVIEW",
  SUBMITTED_FENIX = "SUBMITTED_FENIX",
}

export default class DefenseWorkflowDto {
  id?: number;
  thesisWorkflowId?: number;
  state?: DefenseState;
  scheduledDate?: LocalDate;
  grade?: number;

  constructor(obj?: Partial<DefenseWorkflowDto>) {
    Object.assign(this, obj);
  }
}