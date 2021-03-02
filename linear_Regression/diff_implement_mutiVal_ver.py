import numpy as np
# x_data = np.array([1,2,3,4,5]).reshape(5,1)
# t_data = np.array([2,3,4,5,6]).reshape(5,1)

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

x_data = np.array(training_data)[:,0]
t_data = np.array(training_data)[:,1]

x_data = x_data.reshape(len(x_data),1)
t_data = t_data.reshape(len(t_data),1)

# W,b 값을 랜덤값으로 초기화
# w = np.random.rand(1,1)
# b = np.random.rand(1)

# 학습데이터가 여러개(2개)일 경우
# w = np.random.rand(2,1)

w = np.array([[0.7530408104109766]])
b = np.array([0.6510716300947178])

#print("w=",w,", w.shape=",w.shape,", b=", b, ", b.shape=",b.shape)

def loss_func(x,t):
    y = np.dot(x,w) + b
    return (np.sum((t-y)**2))/(len(x))

def gradient_calcul(f, x):
    delta_x = 1e-4 #매우 작은 값
    grad = np.zeros_like(x) #수치미분된 값을 저장할 ndarray, x와 형상이 같은 배열을 생성
        
    for idx in range(len(x)):
        tmp_val = x[idx]
        # f(x+h) 계산
        x[idx] = tmp_val+delta_x
        fxh1 = f(x)
        
        # f(x-h) 계산
        x[idx] = tmp_val-delta_x
        fxh2 = f(x)    

        grad[idx] = (fxh1-fxh2)/(2*delta_x)
        x[idx] = tmp_val

    return grad

learning_rate = 1e-5
f = lambda x: loss_func(x_data, t_data)
print("initial error value=", loss_func(x_data, t_data), ", initial w=", w, "\nb=",b)


for step in range(10001):
    w -= learning_rate*gradient_calcul(f,w)
    b -= learning_rate*gradient_calcul(f,b)
    
    if(step%400==0):
        print("step=",step,", error value=", loss_func(x_data, t_data), " w=",w," b=",b)

def predict_func(x):
    # 다중일 경우 input은 [x1, x2, x3] 형태
    y = np.dot(x,w)+b
    return y



