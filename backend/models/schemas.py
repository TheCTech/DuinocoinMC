from pydantic import BaseModel

# Request model: when the plugin requests a deposit
class Deposit(BaseModel):
    sender_minecraft_username: str  # Username in minecraft for logging purposes
    transaction_hash: str       # Hash to look for

# Request model: when the plugin requests a withdrawal
class Withdraw(BaseModel):
    recipient_minecraft_username: str  # Username in minecraft for logging purposes
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