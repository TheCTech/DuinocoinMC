from fastapi import FastAPI

from models.schemas import Deposit, DepositApproval, Withdraw, WithdrawalConfirmation
from api.duino import get_balance, validate_deposit
from config import MAX_WITHDRAW_AMOUNT, MIN_WITHDRAW_AMOUNT

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
    amount, message = validate_deposit(data.transaction_hash)

    if amount == 0:
        return DepositApproval(**{
            "success": False,
            "message": message,
            "amount": 0 
        })
    
    return DepositApproval(**{
            "success": True,
            "message": message,
            "amount": amount 
        })

@app.post("/withdraw")
def withdraw(data: Withdraw) -> WithdrawalConfirmation:
    """
    Process a withdrawal request.

    Called by the plugin to send DUCO from the wallet
    to a specified DUINO network user.
    """

    if data.amount < MIN_WITHDRAW_AMOUNT:
        return WithdrawalConfirmation(
            success=False,
            message=f"Withdrawal amount too low, min: {MIN_WITHDRAW_AMOUNT}",
            transaction_id=0
        )
    
    if data.amount > MAX_WITHDRAW_AMOUNT:
        return WithdrawalConfirmation(
            success=False,
            message=f"Withdrawal amount too high, withdrawals higher than {MAX_WITHDRAW_AMOUNT} have to be made manually by support.",
            transaction_id=0
        )

    current_balance = get_balance()
    if current_balance < data.amount:
        return WithdrawalConfirmation(
            success=False,
            message="Insufficient server balance",
            transaction_id=0
        )

    # Placeholder return logic
    return_data = {
        "success": False,
        "message": "Withdrawals are disabled for now.",
        "transaction_id": 0 
    }
    return WithdrawalConfirmation(**return_data)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)