
# coding: utf-8

# In[64]:


import numpy as np
import pandas as pd
from patsy import dmatrices
from sklearn.neural_network import MLPClassifier
from sklearn.model_selection import train_test_split, cross_val_score
from sklearn import metrics
import matplotlib.pyplot as plt
import random


# In[123]:


data = pd.read_csv('./train.csv')


# In[125]:


columns = data.columns[1:-1]
X = data[columns]
y_raw = np.ravel(data['target'])
X.shape


# In[31]:


rate = []
for i in y_raw:
    if i == 'Class_1': rate.append('1_star')
    if i == 'Class_2' or i == 'Class_3' : rate.append('2_star')
    if i == 'Class_4' or i == 'Class_5': rate.append('3_star')
    if i == 'Class_6' or i == 'Class_7': rate.append('4_star')
    if i == 'Class_8' or i == 'Class_9': rate.append('5_star')


# In[48]:


y = np.ravel(rate)


# In[59]:


get_name = pd.read_csv('./epi_r_full.csv')


# In[77]:


name = get_name.iloc[:,14:].columns
name = name[1:94]


# In[84]:


X.columns = name


# In[92]:


X_train_split, X_test_split, Y_train_split, Y_test_split = train_test_split(X, y, test_size = 0.2, random_state = 0)


# In[85]:


fig = plt.figure()
ax = fig.add_subplot(111) # 1 row, 1 col, 1st plot
cax = ax.matshow(X.corr(), interpolation='nearest')
fig.colorbar(cax)
plt.show()


# In[86]:


model = MLPClassifier(solver='lbfgs', alpha=1e-5, hidden_layer_sizes = (30, 10), random_state = 1, verbose = True)


# In[93]:


model.fit(X_train_split, Y_train_split)


# In[98]:


test_prob = model.predict_proba(X_test_split)


# In[99]:


solution = pd.DataFrame(test_prob, columns=['1_star','2_star','3_star','4_star','5_star'])


# In[101]:


solution


# In[120]:


solution.to_csv('./prediction.csv', index = False)


# In[103]:


pred = model.predict(X_test_split)


# In[104]:


metrics.accuracy_score(pred, Y_test_split)


# In[109]:


conf = metrics.confusion_matrix(pred, Y_test_split)


# In[119]:


print metrics.classification_report(pred, Y_test_split)


# In[121]:


solution

