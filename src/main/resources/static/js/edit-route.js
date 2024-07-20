/*function addStation() {
	var stations = document.getElementById('stations');

	var stationCount = stations.children.length;

	var newStationDiv = document.createElement('div');
	newStationDiv.innerHTML = `
					<div>
						<label>Mid Station:</label><br>
						<input type="text" name="stations[${stationCount}].name" placeholder="Enter Mid Station" required>
						<input type="text" name="stations[${stationCount}].price" placeholder="Enter Ticket Price" required>
						<input type="time" name="stations[${stationCount}].time">
						<button type="button" onclick="removeStation(this)">Remove Station</button>
					</div>
				`;

	stations.appendChild(newStationDiv);
}

function removeStation(button) {
	var stationDiv = button.parentNode;
	stationDiv.parentNode.removeChild(stationDiv);
}*/

function addStation() {
	const stationDiv = document.createElement('div');
	stationDiv.innerHTML = `
        <label>To:</label><br>
        <input type="text" name="stations[__index__].name" placeholder="Enter Station">
        <input type="text" name="stations[__index__].price" placeholder="Enter Ticket Price">
        <input type="time" name="stations[__index__].time">
    `;
	document.getElementById('stations').appendChild(stationDiv);
	updateStationIndexes();
}

function updateStationIndexes() {
	const stationDivs = document.querySelectorAll('#stations > div');
	stationDivs.forEach((div, index) => {
		const inputs = div.querySelectorAll('input');
		inputs.forEach(input => {
			input.name = input.name.replace('__index__', index);
		});
	});
}
