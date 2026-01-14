// ========== counterSlice.js ==========
import { createSlice } from '@reduxjs/toolkit';

const counterSlice = createSlice({
  name: 'counter',
  initialState: { value: 0 },
  reducers: {
    increment: (state) => {
      state.value += 1;
    },
    addAmount: (state, action) => {
      state.value += action.payload;  // payload = the number you send
    }
  }
});

export const { increment, addAmount } = counterSlice.actions;
export default counterSlice.reducer;


// ========== Counter.jsx ==========
import { useSelector, useDispatch } from 'react-redux';
import { increment, addAmount } from './counterSlice';

function Counter() {
  // READ from store
  const count = useSelector((state) => state.counter.value);
  
  // GET the sender function
  const dispatch = useDispatch();

  return (
    <div>
      <p>Count: {count}</p>
      
      {/* SEND actions to store */}
      <button onClick={() => dispatch(increment())}>
        +1
      </button>
      
      <button onClick={() => dispatch(addAmount(5))}>
        +5
      </button>
      {/*                              ↑
                            This 5 becomes action.payload */}
    </div>
  );
}

/* 
---

## Visual Flow
```
   You click button
         │
         ▼
   dispatch(addAmount(5))
         │
         │  ┌─────────────────────────┐
         └─►│  ACTION CREATED:        │
            │  {                      │
            │    type: 'counter/addAmount',
            │    payload: 5    ◄───────── The number you passed
            │  }                      │
            └───────────┬─────────────┘
                        │
                        ▼
            ┌───────────────────────┐
            │  REDUCER RUNS:        │
            │                       │
            │  state.value += action.payload
            │  state.value += 5     │
            │                       │
            └───────────┬───────────┘
                        │
                        ▼
            ┌───────────────────────┐
            │  STORE UPDATES:       │
            │  { value: 5 }         │
            └───────────┬───────────┘
                        │
                        ▼
            Component re-renders with new value! */