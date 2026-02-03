from pydantic import BaseModel

# Request model: when the plugin requests a deposit
class Deposit(BaseModel):
    sender_duino_username: str  # Username on duino network

# Request model: when the plugin requests a withdrawal
class Withdraw(BaseModel):
    recipient_username: str     # Username on duino network
    amount: float               # Amount of DUCO being withdrawn

# Response model: deposit confirmation
class DepositApproval(BaseModel):
    success: bool               # True if deposit was successfully detected
    message: str                # Additional message
    amount: float               # Amount deposited

# Response model: withdrawal confirmation
class WithdrawalConfirmation(BaseModel):
    success: bool               # True if withdrawal was completed
    message: str                # Additional message
    transaction_id: int         # Transaction id on the duino network