import numpy as np

# x_train = np.array([1., 2., 3., 4., 5., 6.])
# y_train = np.array([9., 12., 15., 18., 21., 24.])

training_data = [[100, 20], 
		[150, 24], 
		[300, 36], 
		[400, 47], 
		[130, 22], 
		[240, 32],
		[350, 47], 
		[200, 42], 
		[100, 21], 
		[110, 21], 
		[190, 30], 
		[120, 25], 
		[130, 18], 
		[270, 38], 
		[255, 28]]
x_train = np.array(training_data)[:,0]
y_train = np.array(training_data)[:,1]

W = 0.7530408104109766
b = 0.6510716300947178

n_data = len(x_train)   
epochs = 1000          # 전체 데이터 셋에 대한 한 번 학습하는 사이클 = 에폭
learning_rate = 1e-5    # 학습속도와 예측 정확도를 결정하는 파라미터

for i in range(epochs):
    hypothesis = x_train * W + b    # 예측함수 y = Wx + b 형태 -> 식 정의
    cost = np.sum((hypothesis - y_train) ** 2) / n_data # 오차, 평균제곱 오차 MSE

    # gradient_w는 목적 함수인 평균 제곱 오차를 W에 대해 편미분해 계산한 값이며 경사하강법을 적용해 
    # 다음 W를 학습률과 곱해 업데이트한다.
    # 마찬가지로 gradient_b는 b에 대한 편미분 계산값이다.
    
    gradient_w = np.sum((W * x_train - y_train + b) * 2 * x_train) / n_data 
    gradient_b = np.sum((W * x_train - y_train + b) * 2) / n_data

    W -= learning_rate * gradient_w
    b -= learning_rate * gradient_b
    
    if i % 100 == 0:
        print('Epoch ({:10d}/{:10d}) cost: {:10f}, W: {:10f}, b:{:10f}'.format(i, epochs, cost, W, b))

print('W: {:10f}'.format(W))
print('b: {:10f}'.format(b))
print('result : ')
print(x_train * W + b)