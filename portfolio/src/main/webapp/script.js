// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomFact() {
  const defaultBackgroundColor = 'var(--default-color)';
  changeBackgroundColor(defaultBackgroundColor);

  const favorites =
      ['My favorite TV show is "Sherlock"', 'My favorite movie is "The Nightmare before Christmas"', 
      'My favorite color is purple', 'My favorite food is pizza', 'My favorite animals are penguins', 
      'My favorite board game is called "Betrayal at the House on the Hill"', 'Boo!'];

  const favorite = favorites[Math.floor(Math.random() * favorites.length)];

  if(favorite === 'My favorite color is purple'){
      changeBackgroundColor('purple');
  }
  const favoriteContainer = document.getElementById('favorite-container');
  favoriteContainer.innerText = favorite;
}

function changeBackgroundColor(color){
    document.body.style.background = color;
}

function getComments(){
    fetch('/comments')
        .then(response => response.json())
        .then((comments) => {
            const commentContainer = document.getElementById('comment-container');
            comments.forEach((comment) => {
                commentContainer.appendChild(createListElement(comment));
            });
    });
}

function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}