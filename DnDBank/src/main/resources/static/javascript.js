function updateAmount(fieldId, valueChange) {
	const inputField = document.getElementById(fieldId);
	let currentValue = parseInt(inputField.value, 10);

	if (isNaN(currentValue)) {
		currentValue = 0;
	}

	const newValue = currentValue + valueChange;

	if (newValue >= 0) {
		inputField.value = newValue;
	} else {
		inputField.value = 0;
	}
}

function validateNumberInput(input) {
	input.value = input.value.replace(/[^0-9]/g, '');
}

function updateWalletOptions() {
    const receiverSelect = document.getElementById('receiverSelect');
    const walletSelect = document.getElementById('walletSelect');
    const campaignId = parseInt(document.getElementById('campaignId').value);

    const selectedUser = receiverSelect.value;
	
    walletSelect.disabled = '';

    if (selectedUser) {
        fetch(`/getUserWallets?username=${selectedUser}&campaignId=${campaignId}`)
            .then(response => response.json())
            .then(data => {
                data.wallets.forEach(wallet => {
                    const option = document.createElement('option');
                    option.value = wallet.id;
                    option.text = wallet.name;
                    walletSelect.appendChild(option);
                });
            })
            .catch(error => console.error('Error fetching wallets:', error));
    }
}


