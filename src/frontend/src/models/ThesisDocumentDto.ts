import { LocalDate } from "@/types";

export default class ThesisDocumentDto {
  id?: number;
  name?: string;
  content?: Uint8Array;
  thesisWorkflowId?: number;
  uploadDate?: LocalDate;

  constructor(obj?: Partial<ThesisDocumentDto>) {
    Object.assign(this, obj);
  }
}