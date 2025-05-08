package com.bipul.note_taking_app.controllers

import com.bipul.note_taking_app.controllers.NoteController.NoteResponse
import com.bipul.note_taking_app.database.model.Note
import com.bipul.note_taking_app.database.repository.NoteRepository
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.bson.types.ObjectId
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant


// POST https://localhost:8080/notes
// GET https://localhost:8080/notes?ownerId=123
// DELETE https://localhost:8080/notes/123

@RestController
@RequestMapping("/notes")
class NoteController(
    private val noteRepository: NoteRepository
) {

    data class NoteRequest(
        val id: String?,
        @field:NotBlank(message = "Title cannot be blank.")
        val title: String,
        val content: String,
        val color: Long
    )

    data class NoteResponse(
        val id: String,
        val title: String,
        val content: String,
        val color: Long,
        val createdAt: Instant
    )




    @PostMapping
    fun save(
       @Valid @RequestBody body: NoteRequest
    ): NoteResponse{
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String

       val note =  noteRepository.save(
            Note(
                id = body.id ?.let { ObjectId(it) } ?: ObjectId.get(),
                title = body.title,
                content = body.content,
                color = body.color,
                createdAt = Instant.now(),
                ownerId = ObjectId(ownerId)
            )

       )

        return note.toResponse()

        /*return NoteResponse(
           id = note.id.toString(),
           title = note.title,
           content = note.content,
           color = note.color,
           createdAt = note.createdAt
        )*/
    }

    @GetMapping
    fun findByOwnerId(
        //@RequestParam(required = true) ownerId: String
    ): List<NoteResponse>{
        //get ownerId from security
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        return noteRepository.findByOwnerId(ObjectId(ownerId)).map {
            /*NoteResponse(
                id = it.id.toString(),
                title = it.title,
                content = it.content,
                color = it.color,
                createdAt = it.createdAt
            )*/
            it.toResponse()
        }
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteById(@PathVariable id: String){
        val note = noteRepository.findById(ObjectId(id)).orElseThrow {
            IllegalArgumentException("Note not found.")
        }
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        if(note.ownerId.toHexString() == ownerId){
            noteRepository.deleteById(ObjectId(id))
        }
    }
}

private fun Note.toResponse(): NoteController.NoteResponse{
    return NoteResponse(
        id = id.toString(),
        title = title,
        content = content,
        color = color,
        createdAt = createdAt
    )
}
