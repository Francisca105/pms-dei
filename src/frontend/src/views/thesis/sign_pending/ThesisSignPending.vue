<!-- TODO: Pdf submit -->
<template>
    <v-row align="center">
        <v-col>
            <h2 class="text-left ml-1">Teses Pendentes de Assinatura</h2>
        </v-col>
    </v-row>

    <v-text-field
        v-model="search"
        label="Pesquisar"
        prepend-inner-icon="mdi-magnify"
        variant="outlined"
        hide-details
        single-line
    ></v-text-field>

    <v-data-table
        :headers="headers"
        :items="pendingTheses"
        :search="search"
        :loading="loading"
        item-key="id"
        class="text-left"
        no-data-text="Sem teses pendentes de assinatura."
    >
        <template v-slot:item.student="{ item }">
            {{ item.student.name }} ({{ item.student.istId }})
        </template>
        <template v-slot:item.jury="{ item }">
            <ul>
                <li v-for="teacher in item.jury" :key="teacher.id">
                    {{ teacher.name }} ({{ teacher.istId }})
                </li>
            </ul>
        </template>
        <template v-slot:item.president="{ item }">
            {{ item.president ? item.president.name : 'N/A' }}
        </template>
        <template v-slot:item.actions="{ item }">
            <v-btn color="primary" @click="openSignModal(item)">Assinar Documento</v-btn>
        </template>
    </v-data-table>

    <v-dialog v-model="signModalOpen" max-width="600">
        <v-card>
            <v-card-title>
                <span class="headline">Assinar Documento da Tese</span>
            </v-card-title>
            <v-card-text>
                <p>
                    Você está prestes a assinar o documento da tese de
                    <strong>{{ currentThesis.student.name }}</strong
                    >.
                </p>
                <p>Confirme abaixo para prosseguir.</p>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="error" @click="closeSignModal">Cancelar</v-btn>
                <v-btn color="primary" @click="signDocument">Confirmar Assinatura</v-btn>
                <v-btn color="secondary" @click="downloadDocument">Baixar Documento</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script setup lang="ts">
import type ThesisDto from '@/models/ThesisDto'
import RemoteService from '@/services/RemoteService'
import { reactive, ref } from 'vue'

const search = ref('')
const loading = ref(true)
const signModalOpen = ref(false)
const currentThesis = ref<ThesisDto | null>(null)

const headers = [
    { title: 'ID', key: 'id', sortable: true, filterable: false },
    {
        title: 'Estudante',
        key: 'student',
        sortable: true,
        filterable: true
    },
    {
        title: 'Júri',
        key: 'jury',
        sortable: true,
        filterable: true
    },
    {
        title: 'Presidente do Júri',
        key: 'president',
        sortable: true,
        filterable: true
    },
    {
        title: 'Ações',
        key: 'actions',
        sortable: false,
        filterable: false
    }
]

const pendingTheses = reactive<ThesisDto[]>([])

getPendingTheses()
async function getPendingTheses() {
    try {
        const thesisData = await RemoteService.getThesisPresidentAssigned()
        pendingTheses.splice(0, pendingTheses.length)
        pendingTheses.push(...thesisData)
    } catch (error) {
        console.error('Error fetching pending theses:', error)
    } finally {
        loading.value = false
    }
}

function openSignModal(thesis: ThesisDto) {
    currentThesis.value = thesis
    signModalOpen.value = true
}

function closeSignModal() {
    signModalOpen.value = false
    currentThesis.value = null
}

async function signDocument() {
    if (!currentThesis.value) return

    try {
        await RemoteService.signThesisDocument(currentThesis.value.id, null)
        closeSignModal()
        getPendingTheses()
    } catch (error) {
        console.error('Error signing thesis document:', error)
    }
}

async function downloadDocument() {
    if (!currentThesis.value) return

    try {
        const link = document.createElement('a')
        link.href = `/public/DocumentoOficial.pdf`
        link.setAttribute('download', `thesis_${currentThesis.value.id}.pdf`)
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
    } catch (error) {
        console.error('Error downloading thesis document:', error)
    }
}
</script>
