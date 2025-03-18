package fr.isen.RAVAN.isensmartcompanion.dataBase
import java.time.LocalDate

fun generateDummyEvents(): List<Event> {
    return listOf(
        Event(1, "Soirée BDE", "Une soirée de folie organisée par le BDE !", LocalDate.of(2024, 5, 10), "ISEN Toulon", "Soirée"),
        Event(2, "Gala de l'ISEN", "Le grand gala annuel de l'école.", LocalDate.of(2024, 6, 15), "Palais Neptune", "Gala"),
        Event(3, "Journée de cohésion", "Une journée pour renforcer les liens entre les élèves.", LocalDate.of(2024, 4, 20), "Plage du Mourillon", "Cohésion"),
        Event(4, "Tournoi sportif", "Un tournoi sportif entre les différentes promotions", LocalDate.of(2024, 4, 27), "Complexe sportif ISEN", "Sport"),
        Event(5, "Conférence sur l'IA", "Conférence sur le sujet de l'intelligence artificiel", LocalDate.of(2024, 7, 1), "Amphi ISEN", "Conférence"),
        Event(6, "Soirée gaming", "Une soirée de folie sur le theme du gaming", LocalDate.of(2024, 8, 10), "Salle info ISEN", "Soirée"),
    )
}