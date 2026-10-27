const canvas = document.getElementById("graph");
const ctx = canvas.getContext("2d");

const centerX=150;
const centerY=150;

const R= 100;

function drawGraph(rValue) {
    ctx.clearRect(0, 0, canvas.width, canvas.height );
    ctx.fillStyle = "#3394f4ff";

    //рисунок круга
    ctx.beginPath();

    ctx.arc(centerX, centerY, R/2, 0, 0.5*Math.PI);
    ctx.lineTo(centerX, centerY);
    ctx.closePath();
    ctx.fill();

    // рисунок прямоугольника
    ctx.fillRect(centerX,centerY - R, R/2, R);

    // рисунок треугольника
    ctx.beginPath();
    ctx.moveTo(centerX,centerY);
    ctx.lineTo(centerX-(R/2),centerY)
    ctx.lineTo(centerX, centerY + (R/2));

    ctx.closePath();
    ctx.fill();

    // оси Х и У
    ctx.strokeStyle = "black";
    ctx.beginPath();

    ctx.moveTo(0, centerY);
    ctx.lineTo(300, centerY);
    ctx.moveTo(centerX,0);
    ctx.lineTo(centerX,300);
    ctx.stroke();
    
    ctx.beginPath();
    ctx.moveTo(290, centerY - 5 );
    ctx.lineTo(300, centerY);
    ctx.lineTo(290, centerY + 5);

    ctx.moveTo(centerX -5 , 10);
    ctx.lineTo(centerX,0);
    ctx.lineTo(centerX+5, 10)
    ctx.stroke();

    ctx.fillStyle = "black";
    ctx.font = "12px Arial";

    ctx.fillText("X", 287, centerY + 18);
    ctx.fillText("Y", centerX+10, 12);

    // деления на осях
    ctx.beginPath();
    ctx.strokeStyle="black";
    ctx.font = "12px Arial";
    ctx.fillStyle= "black";

    let textR = rValue ? rValue : "R";
    let textHalfR = rValue ? (rValue / 2) : "R/2";
    let textMinusHalfR = rValue ? (-rValue / 2) : "-R/2";
    let textMinusR = rValue ? (-rValue) : "-R";

    const divisions = [
        {pos: R, text: textR},
        {pos: R/2, text: textHalfR},
        {pos: -R/2, text: textMinusHalfR},
        {pos: -R, text: textMinusR}
    ];

    divisions.forEach( division => {
        ctx.moveTo(centerX + division.pos, centerY-3);
        ctx.lineTo(centerX + division.pos, centerY+3);
        ctx.textAlign = "center";
        ctx.fillText(division.text, centerX + division.pos , centerY - 10);

        ctx.moveTo(centerX -3, centerY- division.pos);
        ctx.lineTo(centerX + 3, centerY- division.pos);
        ctx.textAlign = "left";
        ctx.fillText(division.text, centerX + 10, centerY - division.pos +4);
    });
    ctx.stroke();
    drawAllSavedPoints(rValue);
}

drawGraph();

const rCheckboxes = document.querySelectorAll('input[name="r_val"]');

rCheckboxes.forEach(checkbox =>{
    checkbox.addEventListener('change', function(){
        const checkedR = document.querySelectorAll('input[name="r_val"]:checked');
        if (checkedR.length > 0){
            const lastVal = parseFloat(checkedR[checkedR.length - 1].value);
            drawGraph(lastVal);
        } else {
            drawGraph();
        }
    })
} )

const form = document.getElementById('pointForm');


function showError(elementId, text){
    let errElem = document.getElementById(elementId);
    if (errElem){
        errElem.textContent = text;
        errElem.style.display = 'block';
    }

}

function clearAllErrors(){
    ['x-error', 'y-error', 'r-error', 'server-error'].forEach(id => {
        let elem = document.getElementById(id);
        if (elem){
            elem.textContent = '';
            elem.style.display = 'none';
        }
    });

}

form.addEventListener('submit', function(event){

    event.preventDefault();

    clearAllErrors();
    // Валидация X
    const xChecked = document.querySelectorAll('input[name="x_val"]:checked');

    if (xChecked.length === 0){
        showError('x-error', "Нужно выбрать хотя бы одно значение X");
        return;
    }

    const xValues = Array.from(xChecked).map(cb => parseFloat(cb.value));


    // Валидация Y
    const yStr = document.getElementById('y_val').value.trim().replace(',', '.');
    if (yStr === ""){
        showError('y-error', "Нужно ввести значения для Y");
        return;
    }
    let yNum;
    if (yStr.includes("/")){
        const yParse= yStr.split("/");
        const chislitel = Number(yParse[0]);
        const znamenatel = Number(yParse[1]);

        if (yParse.length === 2 && !isNaN(chislitel) && !isNaN(znamenatel) && znamenatel !==0){
            yNum = Number((chislitel/ znamenatel).toFixed(3));
        } else {
            yNum = NaN;
        } 
    } else {
        yNum= Number(yStr);
    }

    if (isNaN(yNum)) {
        showError('y-error', "Значение Y должно быть числом");
        return;
    }

    if (yNum <= -3 || yNum >=5){
        showError('y-error', "Y должно быть в диапозоне от -3 до 5");
        return;
    }


    // Валидация R

      const rChecked = document.querySelectorAll('input[name="r_val"]:checked');

    if (rChecked.length === 0){
        showError('r-error', "Нужно выбрать хотя бы одно значение R");
        return;
    }

    const rValues = Array.from(rChecked).map(cb => parseFloat(cb.value));


    // Генерация всех точек

    const requestData = {
        x: xValues,
        y: yNum,
        r: rValues
    };

    const submitBtn = form.querySelector('button[type="submit"]');
    submitBtn.disabled=true;
    submitBtn.textContent = "Проверка";

    fetch('/fcgi-bin/server.jar',{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
    })
    .then(response => {
        if (!response.ok){
            throw new Error(`Ошибка сервера: ${response.status} ${response.statusText}`);
        }
        return response.json();
    })
    .then(data =>{
        if (data.status ==="error"){
            showError('y-error', data.message || "Ошибка валидации на сервере");
            return;
        }

        if (data.results && Array.isArray(data.results)){
            data.results.forEach(point => {
                const hitText = point.hit ? "Попадание" : "Промах";

                const resultData = {
                    x: point.x,
                    y: point.y,
                    r: point.r,
                    hit: hitText,
                    currentTime: point.currentTime,
                    executionTimeNs: point.executionTimeNs
                };
                
                addRowToTable(resultData);
                saveToLocalStorage(resultData);
            });
            
            // Рисуем график под последний радиус
            const lastR = rValues[rValues.length -1];
            drawGraph(lastR)
        }
    })
    .catch(error =>{
        console.error("AJAX Error:", error);
        showError('server-error',"Не удалось связаться с сервером. Возможно, что Apache и FastCGI не запущены (" + error.message + " )");
    })
    .finally(() => {
        submitBtn.disabled = false;
        submitBtn.textContent = "Проверить точку";
    });

});



function addRowToTable(data) {
    let timeStr = data.currentTime || new Date(data.time).toLocaleString("ru-RU");
    let tableBody = document.querySelector('#resultsTable tbody');
    let execTime = data.executionTimeNs !== undefined ? `${data.executionTimeNs} нс` : "-";
    let newRow = `
    <tr>
    <td>${data.x}</td>
    <td>${data.y}</td>
    <td>${data.r}</td>
    <td>${data.hit}</td>
    <td>${timeStr}</td>
    <td>${execTime}</td>
    </tr>
    `;
    tableBody.innerHTML += newRow;
}

function saveToLocalStorage(data) {
    let savedData = localStorage.getItem("pointsHistory_505284");

    let pointsArray = savedData ? JSON.parse(savedData) : [];

    pointsArray.push(data);

    localStorage.setItem("pointsHistory_505284", JSON.stringify(pointsArray));

}

function loadFromLocalStorage(){
    let savedData = localStorage.getItem("pointsHistory_505284");
    if (savedData) {
        let pointsArray = JSON.parse(savedData);
        pointsArray.forEach(function(point){
            addRowToTable(point);
        });
    }
}

loadFromLocalStorage();

const clearBtn = document.getElementById('clearTableBtn');

clearBtn.addEventListener('click', function(){
    
    localStorage.removeItem("pointsHistory_505284");

    let tableBody = document.querySelector('#resultsTable tbody');
    tableBody.innerHTML="";

    const checkedR = document.querySelectorAll('input[name="r_val"]:checked');
    const currentR = checkedR.length > 0 ? parseFloat(checkedR[checkedR.length - 1].value) : undefined;
    drawGraph(currentR);
    
});

function drawPoint(x, y, r, isHit){
    let canvasX = centerX + (x/r)*R;
    let canvasY = centerY - (y/r) *R;

    ctx.beginPath();
    ctx.arc(canvasX, canvasY, 4, 0, 2*Math.PI);

    ctx.fillStyle = isHit ? "#02e376ff" : "#f03a03ff";
    ctx.fill();
    ctx.closePath()
}

function drawAllSavedPoints(currentR){
    if (!currentR) return;

    let savedData = localStorage.getItem("pointsHistory_505284");
    if (savedData) {
        let pointsArray = JSON.parse(savedData);

        pointsArray.forEach(function(point){
            if (point.r === currentR){
                let isHitNow = (point.hit === "Попадание");
                drawPoint(point.x, point.y, currentR, isHitNow);
            }
        
        });
    }
}