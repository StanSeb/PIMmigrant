//Creating global variables to make multiple methods call them
let notesArray = [];
let theNoteObjectId = null;
let file = null;


// Attach file button
Array.prototype.forEach.call(document.querySelectorAll(".file-upload__button"), function (button) {
    const hiddenInput = button.parentElement.querySelector(".file-upload__input");
    const label = button.parentElement.querySelector(".file-upload__label");
    const defaultLabelText = "No file(s) selected";

    let htmlImage = document.querySelector("#image-place");
    let htmlFile = document.querySelector("#file-place");

    htmlImage.src = "";
    htmlFile.href = "";
    htmlFile.innerHTML = "";

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


// Communication between JavaScript and the Server 
async function getNotes() {
    let result = await fetch("/rest/Notes");  // Answer from the Server
    notesArray = await result.json();  // Converting from json to JavaScript object

    
    // Every fetch looks like this
    renderNotes();
}


function renderNotes() {
    var noteList = document.querySelector("#notes-list"); // Element containing Notes

    noteList.innerHTML = ""; // Empty note list

    // This block of code will loop all notes from notesArray into noteList.innerHTML
    // and adding the whole <li> element 
    for (let note of notesArray) {

        let date = new Date(note.timestamp).toLocaleString();
        let noteLi = `<li class="note-li" onclick="noteClicked(${note.id})">
                        <div class="noteli-text">
                        <h2>${note.title}</h2>
                        <h3 id="date">${date}</h3>
                        <p>${note.content}</p>
                        </div>`;

        // if-else statement that makes an file-icon if the added file is not an image
        // and making a thumbnail if it is an image               
        if(note.imgUrl ===""){
            note.imgUrl = "";
        }                

        else if(note.imgUrl.includes(".jpeg") || note.imgUrl.includes(".PNG") || note.imgUrl.includes(".svg") ||
                note.imgUrl.includes(".TIFF") || note.imgUrl.includes(".BMP") || note.imgUrl.includes(".jpg")) {
            noteLi += `<img src=${note.imgUrl} class="thumbnail"></li>`;
        }else{
            noteLi += `<i class="far fa-file"></i></li>`;
        } 

        //Adding the note to the list of notes
        noteList.innerHTML += noteLi;
        
    }
}


//Method for getting the id of a note
function getNoteById(noteId) {
    for(note of notesArray) {
        if(note.id == noteId){
            return note;
        } 
    }
}

//Method for getting the note by calling the method "getNoteById"
// when clicking on the note in the notes list
async function noteClicked(noteId) {

    //Declaring the HTML ids
    let htmlContent = document.querySelector("#note");
    let htmlTitle = document.querySelector("#title");
    let htmlImage = document.querySelector("#image-place");
    let htmlFile = document.querySelector("#file-place");

    htmlImage.src = "";
    htmlFile.href = "";
    htmlFile.innerHTML = "";


    //Making a note variable with the id of the note we have clicked
    let note = getNoteById(noteId);
    
        let result = await fetch("/rest/Notes/" + note.id, {
            method: "GET"
        });

        //Getting the note back
        let noteObject=await result.json();

        //Declaring the note object variables in JavaScript
        theNoteObjectId = noteObject.id;
        let title = noteObject.title;
        let content = noteObject.content;
        file = noteObject.imgUrl;

            htmlContent.value = content;
            htmlTitle.value = title;

            //If-else statement for getting either an image or a file back
            if(file.includes(".jpeg") || file.includes(".PNG") || file.includes(".svg") ||
                file.includes(".TIFF") || file.includes(".BMP") || file.includes(".jpg")) {
                    htmlImage.src = file;
                    htmlFile.href = null;
                    getNotes();
            }
            else{
                htmlImage.src = null;
                htmlFile.href = file;
                htmlFile.innerHTML = file;
                getNotes();
            } 

}

//Deleting a note when already opened
//This connects via the Server 
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


//Javascript method for adding a note
async function addNote(e) {
    e.preventDefault();

    //Making a variable for the file that will be added to the note
    let files = document.querySelector('input[type=file]').files;
    let formData = new FormData();

    //Adding the specific file to the FormData
    for(let thisFile of files) {
            formData.append('files', thisFile, thisFile.name);
    }
    
    //Uploading the file within the FormData to the Server
    let uploadResult = await fetch('/api/file-upload', {
        method: 'POST',
        body: formData
    });    

    let imageURL = await uploadResult.text();    

    //Declaring the variables for the containers of "Title" and "Note"
    let titleField = document.getElementById("title");
    let inputField = document.getElementById("note");

    let titleFieldValue = titleField.value;
    let inputFieldValue = inputField.value;   
   

    //The body when adding a new note
    let theBody = JSON.stringify(
        {
            title: titleFieldValue,
            content: inputFieldValue,
            imgUrl: imageURL
        });

    //The body when updating a note without an image
    //but adding one image    
    let theBodyUpdateNoImage = JSON.stringify(
        {
            id: theNoteObjectId,
            title: titleFieldValue,
            content: inputFieldValue,
            imgUrl: imageURL
        });

    //The body when updating a note and keeping
    //the image it already has     
    let theBodyUpdateWithImage = JSON.stringify(
        {
            id: theNoteObjectId,
            title: titleFieldValue,
            content: inputFieldValue,
            imgUrl: file
        });

    //Alert the user if the Title and Note values are empty    
    if (titleFieldValue === "" && inputFieldValue === "") {
        alert("Please enter a title and/or a note.");
    }

    //else-if statement for adding a new note
    else if(theNoteObjectId == 0 || theNoteObjectId == null){

        let result = await fetch("/rest/Notes", {
            method: "POST",
            body: theBody
        });
        console.log(await result.text());
        notesArray.push(theBody);
        getNotes();
    }    

    //else-if statement for updating a note
    else if(theNoteObjectId != 0 || theNoteObjectId != null){

        if(file !='' && imageURL!=''){
            let result = await fetch("/rest/Notes/update", {
                method: "POST",
                body: theBodyUpdateNoImage
            });
            console.log(await result.text());
            notesArray.push(theBodyUpdateNoImage);
            getNotes();
        }
    
        else if(file!=''){
            let result = await fetch("/rest/Notes/update", {
                method: "POST",
                body: theBodyUpdateWithImage
            });
            console.log(await result.text());
            notesArray.push(theBodyUpdateWithImage);
            getNotes();
        }  
        
        else if (imageURL!=''){
            let result = await fetch("/rest/Notes/update", {
                method: "POST",
                body: theBodyUpdateNoImage
            });
            console.log(await result.text());
            notesArray.push(theBodyUpdateNoImage);
            getNotes();
        }
        else{
            let result = await fetch("/rest/Notes/update", {
                method: "POST",
                body: theBodyUpdateNoImage
            });
            console.log(await result.text());
            notesArray.push(theBodyUpdateNoImage);
            getNotes();
        }        
    }  

}


//Searchbar function to filter between all notes
//The user can search for the title, contents of the note
//or the date
function search(input) {

    let searchlist = $('.note-li');

    for (let findText of searchlist) {
        let foundText = $(findText).find('h2').text();
        let foundDate = $(findText).find('h3').text();
        let foundContent = $(findText).find('p').text();

        if (foundText.toLowerCase().includes(input.toLowerCase()) || foundDate.includes(input.toLowerCase()) || foundContent.toLowerCase().includes(input.toLowerCase())) {
            $(findText).show();
        } else {
            $(findText).hide();
        }
    }
}


//Function button inside the searchbar that clears the users input
function clearInput() {
    document.getElementById('searchbox').value = "";

    renderNotes();
}