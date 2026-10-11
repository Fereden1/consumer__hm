function updateTaxRate() {
    fetch(window.APP_CONTEXT_PATH + "/api/tax-rate")
        .then(function (response) {
            if (!response.ok) {
                throw new Error("Ошибка получения ставки");
            }
            return response.json();
        })
        .then(function (data) {
            if (data.percent !== undefined) {
                document.getElementById("percent").innerText = data.percent + "%";
            }
        })
        .catch(function (error) {
            console.error("Не удалось обновить ставку:", error);
        });
}
setInterval(updateTaxRate, 3000);
