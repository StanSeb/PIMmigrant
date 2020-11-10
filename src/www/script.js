async function attach(e) {
    e.preventDefault();
    let files = document.querySelector('.add-btn').getNotes;
    let formData = new FormData();
    formData.append('file', file.name);
    await fetch('/upload.php', {method: "POST", body: formData});
    alert("upload complete");
}

let notesArray = [];

// Communication between js and the server
async function getNotes() {
    let result = await fetch("/rest/Notes");  // answer from server
    notesArray = await result.json();  // converting from json to js object
    
    // Every fetch looks like this

    renderNotes();
    assignDeleteButtons();
}

function renderNotes() {
    var noteList = document.querySelector("#notes-list"); // Element containing Notes

    noteList.innerHTML = ""; // Empty note list

    // This block of code will loop all notes from notesArray into noteList.innerHTML, 
    for (let note of notesArray) {
        let date = new Date(note.timestamp).toLocaleString();

        let noteLi =    `<li class="note-li">
                        <h2>${note.title}</h2>
                        <h3 id="date">${date}</h3>
                        <p>${note.content}</p></li>`;

        noteList.innerHTML += noteLi;

    }

    

    /*let allLi = document.getElementsByTagName("li");
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

        saveButton.innerHTML = '<i class="fas fa-save"></i>';
        addButton.innerHTML = '<i class="fas fa-paperclip"></i>';
        deleteButton.innerHTML = '<i class="fas fa-trash"></i>';

        divButtons.appendChild(addButton);
        divButtons.appendChild(saveButton);
        divButtons.appendChild(deleteButton);

        allLi[i].appendChild(divButtons);

    }
    */

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
        alert("The fields can not be empty!");
    } else {

        let result = await fetch("/rest/Notes", {
            method: "POST",
            body: theBody
        });

        console.log(await result.text());

        getNotes();

    }
}

// Attach file button
Array.prototype.forEach.call(document.querySelectorAll(".file-upload__button"), function (button) { 
    const hiddenInput = button.parentElement.querySelector(".file-upload__input");
    const label = button.parentElement.querySelector(".file-upload__label");
    const defaultLabelText = "No file(s) selected";

    // Set default text for label
    label.textContent = defaultLabelText;
    label.title = defaultLabelText;

    button.addEventListener("click", function() {
        hiddenInput.click();
    });

    hiddenInput.addEventListener("change", function() {
        const fileNameList = Array.prototype.map.call(hiddenInput.files, function (file) {
            return file.name;
        });

        label.textContent = fileNameList.join(",")|| defaultLabelText;
    });
});

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

