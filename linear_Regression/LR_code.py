
def solution(X,Y,epoch,lr,W,b):
    
    for i in range(epoch):
        temp_W = 0
        for i in range(len(X)):
            temp_W = temp_W + (X[i] * W + b - Y[i])*X[i] 
        
        temp_b = 0
        for i in range(len(X)):
            temp_b = temp_b + (X[i] * W + b - Y[i]) 
        
        d_W = temp_W*2 / len(X)
        d_b = temp_b*2 / len(X)


        W  = W - lr * (d_W)
        b  = b - lr * (d_b)
    
    return W,b

def cost_func(x,y):
    #pred_Y = Wx + b
    MSE = 0
    for i in range(len(x)):
        MSE += (y[i] - (x[i] * W + b))**2

    return MSE/len(x)

X = [100, 150, 300, 400, 130, 240, 350, 200, 100, 110, 190, 120, 130, 270, 255]
Y = [20, 24, 36, 47, 22, 32, 47, 42, 21, 21, 30, 25, 18, 38, 28]
epoch = 1000
lr = 0.00001
W = 0.7530408104109766
b = 0.6510716300947178

print(solution(X,Y,epoch,lr,W,b))

## 1. 조정된 W, b 값
## 2. 새로운 x값 입력에 따른 예측값