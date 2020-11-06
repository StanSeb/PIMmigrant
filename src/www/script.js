let notesArray = [];

async function getNotes() {
    let result = await fetch("/rest/Notes");
    notesArray = await result.json();

    renderNotes();
    assignDeleteButtons();
}

function renderNotes() {
    var noteList = document.querySelector("#notes-list");

    noteList.innerHTML = "";

    for (let note of notesArray) {
        let noteLi = `<li class="note-li"> <h2>${note.title}</h2> <p>${note.content}</p></li>`;

        noteList.innerHTML += noteLi;
    }

    let allLi = document.getElementsByTagName("li");
    let i;

    for (i = 0; i < allLi.length; i++) {

        const divButtons = document.createElement("DIV");
        const addButton = document.createElement("button");
        const saveButton = document.createElement("button");
        const deleteButton = document.createElement("button");

        addButton.className = "add-btn";
        saveButton.className = "save-btn";
        deleteButton.className = "delete-btn";
        divButtons.className = "div-buttons";

        addButton.innerHTML = '<i class="fas fa-plus"></i>'
        saveButton.innerHTML = '<i class="fas fa-save"></i>';
        deleteButton.innerHTML = '<i class="fas fa-trash"></i>';

        divButtons.appendChild(addButton);
        divButtons.appendChild(saveButton);
        divButtons.appendChild(deleteButton);

        allLi[i].appendChild(divButtons);

    }

}

function assignDeleteButtons() {
    listOfButtons = document.getElementsByClassName("delete-btn");

    var parentToDelete = document.getElementsByClassName("div-buttons");


    for (let index = 0; index < listOfButtons.length; index++) {
        listOfButtons[index].onclick = function () {
            parentToDelete[index].parentElement.style.display = "none";
            deleteNote(index);
        }

    }
}


async function addNote() {

    let titleField = document.getElementById("title");
    let inputField = document.getElementById("note");

    let titleFieldValue = titleField.value;
    let inputFieldValue = inputField.value;

    let theBody = JSON.stringify(
        {title: titleFieldValue,
            content: inputFieldValue 
        });

    if (inputFieldValue == "" && titleFieldValue == "") {
        alert("This field can not be empty");
    } else {

        let result = await fetch("/rest/Notes", {
            method: "POST",
            body: theBody
        });

        console.log(await result.text());

        getNotes();
    }
}

async function deleteNote(index) {

    let noteToDelete = notesArray[index].id;

    let theBody = {
        id: noteToDelete
    }

    console.log(noteToDelete);
    let result = await fetch("/rest/Notes/id", {
        method: "DELETE",
        body: JSON.stringify(theBody)
    });

    renderNotes();
}

function search(input){
    
    let searchlist = $('.note-li');
    
    for(let findTitle of searchlist){
        let foundTitle = $(findTitle).find('h2').text();
        if(foundTitle.toLowerCase().includes(input.toLowerCase())){
            $(findTitle).show();
        }else{
            $(findTitle).hide();
        }
    }
}

function clearInput(){
    document.getElementById('searchbox').value= "";
    
    renderNotes();
}