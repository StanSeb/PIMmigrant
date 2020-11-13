let notesArray = [];
theNoteObjectId= null;

// Communication between js and the server
async function getNotes() {
    let result = await fetch("/rest/Notes");  // answer from server
    notesArray = await result.json();  // converting from json to js object

    // Every fetch looks like this

    renderNotes();
}

function renderNotes() {
    var noteList = document.querySelector("#notes-list"); // Element containing Notes

    noteList.innerHTML = ""; // Empty note list

    // This block of code will loop all notes from notesArray into noteList.innerHTML,
    for (let note of notesArray) {

        let date = new Date(note.timestamp).toLocaleString();
        let noteLi = `<li class="note-li" onclick="noteClicked(${note.id})">
                        <div class="noteli-text">
                        <h2>${note.title}</h2>
                        <h3 id="date">${date}</h3>
                        <p>${note.content}</p>
                        </div>`;

        if(note.imgUrl ===""){
            note.imgUrl = "";
        }

        else if(note.imgUrl.includes(".jpeg") || note.imgUrl.includes(".PNG") || note.imgUrl.includes(".svg") ||
                note.imgUrl.includes(".TIFF") || note.imgUrl.includes(".BMP")) {
            noteLi += `<img src=${note.imgUrl} class="thumbnail"></li>`;
        }else{
            noteLi += `<i class="far fa-file"></i></li>`;
        }

        noteList.innerHTML += noteLi;
    }
}

function getNoteById(noteId) {

    for(note of notesArray) {
        if(note.id == noteId){
            return note;
        }
    }
}

async function noteClicked(noteId) {

    let htmlContent = document.querySelector("#note");
    let htmlTitle = document.querySelector("#title");

    let note = getNoteById(noteId);
    
        let result = await fetch("/rest/Notes/" + note.id, {
            method: "GET"
        });

        let noteObject=await result.json();
        theNoteObjectId = noteObject.id;
        let title = noteObject.title;
        let content = noteObject.content;
        
        htmlContent.value = content;
        htmlTitle.value = title;
     
}


async function deleteNote(){
   
    let theBodyUp = JSON.stringify(
        {
            id: theNoteObjectId,   
        });
            let result = await fetch("/rest/Notes/delete", {
                method: "DELETE",
                body: theBodyUp
            });
            console.log(result);
            notesArray.push(theBodyUp);
            getNotes();
        }  


async function addNote(e) {
    e.preventDefault();

            
    let files = document.querySelector('input[type=file]').files;
    let formData = new FormData();

    for(let file of files) {
        formData.append('files', file, file.name);
    }

    let uploadResult = await fetch('/api/file-upload', {
        method: 'POST',
        body: formData
    });

    let imageURL = await uploadResult.text();


    let titleField = document.getElementById("title");
    let inputField = document.getElementById("note");

    let titleFieldValue = titleField.value;
    let inputFieldValue = inputField.value;

    let theBodyUp = JSON.stringify(
        {
            id: theNoteObjectId,
            title: titleFieldValue,
            content: inputFieldValue,
            imgUrl: imageURL
        });

        let theBody = JSON.stringify(
            {
                title: titleFieldValue,
                content: inputFieldValue,
                imgUrl: imageURL
            });

        if (titleFieldValue === "") {
            alert("Please enter a title.");
        }
        else if (inputFieldValue === ""){
            alert("Please write something in the note window or add a file.")
        }
        else if(theNoteObjectId ==0 || theNoteObjectId == null){
    
            let result = await fetch("/rest/Notes", {
                method: "POST",
                body: theBody
            });
            console.log(await result.text());
            notesArray.push(theBody);
            getNotes();
        }
        else{
            let result = await fetch("/rest/Notes/update", {
                method: "POST",
                body: theBodyUp
            });
            console.log(result);
            notesArray.push(theBodyUp);
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

    button.addEventListener("click", function () {
        hiddenInput.click();
    });

    hiddenInput.addEventListener("change", function () {
        const fileNameList = Array.prototype.map.call(hiddenInput.files, function (file) {
            return file.name;
        });

        label.textContent = fileNameList.join(",") || defaultLabelText;
    });
});

function search(input) {

    let searchlist = $('.note-li');

    for (let findText of searchlist) {
        let foundText = $(findText).find('h2').text();
        let foundDate = $(findText).find('h3').text();
        let foundContent = $(findText).find('p').text();

        if (foundText.toLowerCase().includes(input.toLowerCase()) || 
            foundDate.includes(input) || foundContent.toLowerCase().includes(input.toLowerCase())){
            $(findText).show();
        } else {
            $(findText).hide();
          
        }
    }
}

function clearInput() {
    document.getElementById('searchbox').value = "";

    renderNotes();
}



