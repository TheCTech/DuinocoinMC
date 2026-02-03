from fastapi import FastAPI

from models.schemas import Deposit, DepositApproval, Withdraw, WithdrawalConfirmation

app = FastAPI(title="DuinoMC")


@app.get("/")
def root():
    return {"status": "Backend running"}

@app.post("/deposit")
def deposit(data: Deposit) -> DepositApproval:
    """
    Process a deposit request.

    Called by the plugin to verify or register an incoming DUCO deposit
    for a specific DUINO network user.
    """

    # Placeholder return logic
    return_data = {
        "success": True,
        "message": "aaa",
        "amount": 10 
    }
    return DepositApproval(**return_data)

@app.post("/withdraw")
def withdraw(data: Withdraw) -> WithdrawalConfirmation:
    """
    Process a withdrawal request.

    Called by the plugin to send DUCO from the wallet
    to a specified DUINO network user.
    """

    # Placeholder return logic
    return_data = {
        "success": True,
        "message": "aaa423",
        "transaction_id": 1000023544234432 
    }
    return WithdrawalConfirmation(**return_data)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)