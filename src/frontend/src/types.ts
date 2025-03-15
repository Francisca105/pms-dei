export type LocalDate = string; // DD-MM-YYYY

export function isValidLocalDate(date: string): date is LocalDate {
    const regex = /^\d{2}-\d{2}-\d{4}$/;
    return regex.test(date);
}

export function toLocalDate(date: Date): LocalDate {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${day}-${month}-${year}`;
  }